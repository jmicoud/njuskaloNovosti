package com.dev.stdev.njuskalonovosti;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class AlarmConfigurationService extends IntentService {

    DatabaseClass db = new DatabaseClass(this);

    public AlarmConfigurationService() {
        super("AlarmConfigurationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String alarmGeneralId = intent.getStringExtra(MainActivity.MESSAGE_ALARM);
            //DatabaseClass db = new DatabaseClass(context);

            if(alarmGeneralId!=null)
            {
                Log.d("Alarm TRIGERED: ", "" + alarmGeneralId);

                doGetApartments(alarmGeneralId);
            }

        }

    }



    public void doGetApartments(final String generalid) {

        try {


            List<SearchClass> pr = db.getPretragaByGenID(generalid); //samo jedna pretraga se uvijek vraća

            final String upit = pr.get(0).getPretraga(); //samo jedna pretraga se uvijek vraća

            String ctls = "search_ads";
            String sorts = "new";

            //toast = Toast.makeText(this, "0", Toast.LENGTH_SHORT);
            //toast.show();

            //Log.d("PRIJE","Prija");

            SearchNewFlatAdvertisementsService.NjuskaloService service = SearchNewFlatAdvertisementsService.NjuskaloService.retrofit.create(SearchNewFlatAdvertisementsService.NjuskaloService.class);
            Call<ResponseBody> call = service.getTask(ctls,upit,sorts);

            //Log.d("ALARM LINK", call.request().url().toString());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        try {
                            //Log.d("GOT RESPONSE", "GOT RESPONSE");

                            String msg =  response.body().string();
                            //int lnt = msg.length();

                            parseRespIntoDatabase(msg, upit, generalid);


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



    public void parseRespIntoDatabase(String resp, String upit, String generalid)
    {
        //int locSt = resp.indexOf("Njuškalo oglasi");
        int i, n;

        i = resp.indexOf("data-ad-id");
        n = i;

        String mailStr="";
        FlatAdvertismentClass flRf;
        List<FlatAdvertismentClass> flNewLs = new ArrayList<>();

        while(true)
        {

            i = resp.indexOf("data-ad-id", i+1);

           // Log.d("ISPIS I: ", ""+i+","+n);

            if ((i-n) < 1500) break;

            flRf = parseAllValues(resp.substring(n, i), upit, generalid);


            if(flRf.getIsNewApartment().equals("1")) //only if new apartment put it in list becouse tat list we will send on email
            {
                flNewLs.add(flRf);

                //Log.d("FLAT DES: ",flRf.getDescription());
                //Log.d("ISNEW: ", flRf.getIsNewApartment());
                //Log.d("LISTSIZE: ", ""+flNewLs.size());

            }



            n = i;



        }

        //Log.d("LISTSIZE1: ", ""+flNewLs.size());

        for(int j=0; j<flNewLs.size(); j++)
        {

            mailStr = mailStr + "ID: " + flNewLs.get(j).getId() + "\n" + "STAN: " + flNewLs.get(j).getDescription() + "\n" + "LINK: " + flNewLs.get(j).getLink() + "\n" + "PRIZE: " + flNewLs.get(j).getPrize() + "\n" + "DATE: " + flNewLs.get(j).getDtm() + "\n\n";

        }

        //Log.d("NOVI STANOVI: \n", mailStr);

        sendMail(mailStr, upit);

    }


    public void sendMail(String completeMessage, String upit)
    {

        BackgroundMail.newBuilder(this)
                .withUsername("obavijest.stanovi@gmail.com")
                .withPassword("obavijest123")
                .withMailto("obavijest.stanovi@gmail.com")
                .withSubject("Obavijest - Stanovi: " + upit)
                .withBody(completeMessage)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                        Log.d("MAIL SENT OK", "MAIL SENT OK");
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                        Log.d("MAIL ERROR", "MAIL ERROR");
                    }
                })
                .send();


    }

    public FlatAdvertismentClass parseAllValues(String valStr, String upit, String generalid)
    {
        FlatAdvertismentClass fl = new FlatAdvertismentClass();
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

        //Log.d("\nFLAT DESC:", description);

                //pretraži stanove na temelju general id-a pretrage, usporedi jesu li novi na temelju id-a dodaj nove stanove u bazu i vrati natrag nove plus prepoznate stare(dio istih)
                List<FlatAdvertismentClass> flLs = db.getAllApartments(generalid);

                //Log.d("BROJSTANOVA","BROJ STANOVA: "+flLs.size());

                boolean nwFlat = false;
                for(int i=0; i<flLs.size(); i++)
                {
                    //Log.d("IDES",id + "," + flLs.get(i).getId());

                    if(id.equals(flLs.get(i).getId())) //there is same flat in database for this search
                    {
                        //Log.d("FLATEXIST","FLATEXIST: " + id);
                        nwFlat = true;
                        break;
                    }
                }

                if(nwFlat==false) //new apartment recognized
                {
                    fl.setIsNewApartment("1");
                    db.addApartment(fl,generalid);
                }
                else
                {
                    fl.setIsNewApartment("0");

                }

        return (fl);
        //sendBroadcastMessage("FLAT_BRD", fl);

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




}
