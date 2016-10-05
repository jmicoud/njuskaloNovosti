package com.dev.stdev.njuskalonovosti;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class dohvatStanovaServis extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private dbClass db = new dbClass(this);


    public dohvatStanovaServis() {
        super("dohvatStanovaServis");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Gets data from the incoming Intent
        //String dataString = workIntent.getDataString();
        if (intent != null) {

            Log.d("U servisu dohvati stae", "U SERVISU DOHVATI ST");

            String lnk = intent.getStringExtra("LINK");

            Log.d("LINK", lnk);

            doGetApartments(lnk);

            // Do work here, based on the contents of dataString
        }
    }


    public void doGetApartments(final String upit) {

        try {

            String ctls = "search_ads";
            String sorts = "new";

            //toast = Toast.makeText(this, "0", Toast.LENGTH_SHORT);
            //toast.show();

            //Log.d("PRIJE","Prija");

            NjuskaloService service = NjuskaloService.retrofit.create(NjuskaloService.class);
            Call<ResponseBody> call = service.getTask(ctls,upit,sorts);

            Log.d("LINK", call.request().url().toString());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        try {
                            //Log.d("RESPONSA", response.body().string());

                            String msg =  response.body().string();
                            //int lnt = msg.length();

                            parseRespIntoDatabase(msg, upit);


                        }
                        catch(Exception ex)
                        {


                        }

                        //toast.show();
                        // tasks available
                    } else {
                        // error response, no access to resource?
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // something went completely south (like no internet connection)
                }
            });

        }
        catch (Exception e)
        {
            Log.d("ERROR",  e.getMessage().toString());
        }

    }



    public interface NjuskaloService {

        @GET("/index.php")
        Call<ResponseBody> getTask(@Query("ctl") String ctl, @Query("keywords") String keyw, @Query("sort") String srt);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.njuskalo.hr/")
                .build();

    }



    public void parseRespIntoDatabase(String resp, String upit)
    {
        //int locSt = resp.indexOf("Njuškalo oglasi");
        int i, n;

        i = resp.indexOf("data-ad-id");
        n = i;



        while(i >= 0)
        {

            i = resp.indexOf("data-ad-id", i+1);

            parseAllValues(resp.substring(n, i), upit);
            n = i;

        }


    }


    public void parseAllValues(String valStr, String upit)
    {

        flatData fl = new flatData();
        //Bundle bundle = new Bundle();

        String id, link, dtm, prize, description;
        id=substringBetween(valStr, "ad-id=\"","\"").trim();

        link = substringBetween(valStr, "\" class=\"link\" href=\"", "\">");
        link = "http://www.njuskalo.hr" + link;

        dtm = substringBetween(valStr,"datetime=\"","\" pubdate=").trim();
        prize = substringBetween(valStr,"price price--eur\">"," <span class=\"currency").trim();
        description = substringBetween(valStr,"<div class=\"entity-description-main\">","<br />").trim();

        fl.setDtm(dtm);
        fl.setId(id);
        fl.setLink(link);
        fl.setPrize(prize);
        fl.setDescription(description);


        //if(db.isPretrageTableEmpty()==false)
       // {
            //is this existing search
            boolean isNs = false;
            String tempGenId = "";
            List<pretrageClass> lP = db.getAllPretrage();
            for(int i=0; i<lP.size(); i++)
            {
                if (upit.equals(lP.get(i).getPretraga()))
                {
                    isNs = true; //there is existing search, exit loop
                    tempGenId = lP.get(i).getGeneralId();
                    break;
                }
            }


            if(isNs=false) //this is new search
            {

                //dohvati zadnju pretragu jer ima
              pretrageClass prTm = lP.get(lP.size()-1);
              int newGeneralId = Integer.parseInt(prTm.getGeneralId()) + 1; //zadnji plus 1 je id za novu pretragu
              pretrageClass prNew = new pretrageClass();
              prNew.setGeneralId(Integer.toString(newGeneralId));
              prNew.setPretraga(upit);
              prNew.setTip("0"); //not alarm search
              db.addPretraga(prNew);

              fl.setIsNewApartment("1"); //this isnew apartment becouse it is new search so value is 1

              db.addApartment(fl,Integer.toString(newGeneralId)); //add apartment to database, novistanovi table

                //dodaj novu pretragu - geerirajnovi general id, pretraži stanove na temelju novog id-a, nema ih naravno jer je nova pretraga, ddaj fld.isnew..
            }
            else //isNs=true
            {
                //pretraži stanove na temelju general id-a pretrage, usporedi jesu li novi na temelju id-a dodaj nove stanove u bazu i vrati natrag nove plus prepoznate stare(dio istih)
                List<flatData> flLs = db.getAllApartments(tempGenId);

                boolean nwFlat = false;
                for(int i=0; i<flLs.size(); i++)
                {
                    if(id.equals(flLs.get(i).getId())) //there is same flat in database for this search
                    {
                        nwFlat = true;
                        break;
                    }
                }

                if(nwFlat==false) //new apartment recognized
                {
                    fl.setIsNewApartment("1");
                    db.addApartment(fl,tempGenId);
                }
                else
                {
                    fl.setIsNewApartment("0");
                }


            }

        //}
        //else //Pretrage table is empty
       // {

        //}

        sendBroadcastMessage("FLAT_BRD", fl);




    }


   /* public String genNewGenId()
    {
        String retStr="";



        return (retStr);

    }*/


    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }




    private void sendBroadcastMessage(String intentFilterName, flatData f) {

       // Log.d("Šaljem Intent","Šaljem Intent");

        Intent intent = new Intent(intentFilterName);
        intent.putExtra("FLAT_OBJECT", f);
        sendBroadcast(intent);
    }



}