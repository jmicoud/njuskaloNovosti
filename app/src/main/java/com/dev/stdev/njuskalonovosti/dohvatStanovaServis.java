package com.dev.stdev.njuskalonovosti;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

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
        super("getNewApartmentsService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        //String dataString = workIntent.getDataString();

        String lnk = workIntent.getStringExtra("LINK");

        doGetApartments(lnk);

        // Do work here, based on the contents of dataString

    }


    public void doGetApartments(String upit) {

        try {

            String ctls = "search_ads";
            String sorts = "new";

            //toast = Toast.makeText(this, "0", Toast.LENGTH_SHORT);
            //toast.show();


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

                            parseRespIntoDatabase(msg);


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



    public void parseRespIntoDatabase(String resp)
    {
        //int locSt = resp.indexOf("Njuškalo oglasi");
        int i, n;

        i = resp.indexOf("data-ad-id");
        n = i;



        while(i >= 0)
        {

            i = resp.indexOf("data-ad-id", i+1);

            parseAllValues(resp.substring(n, i));
            n = i;

        }


    }


    public void parseAllValues(String valStr)
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


        sendBroadcastMessage("FLAT_BRD", fl);

        if(db.isApartmentsTableEmpty()==false)
        {


        }


    }



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

        Log.d("Šaljem Intent","Šaljem Intent");

        Intent intent = new Intent(intentFilterName);
        intent.putExtra("FLAT_OBJECT", f);
        sendBroadcast(intent);
    }



}