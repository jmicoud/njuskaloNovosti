package com.dev.stdev.njuskalonovosti;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public class dohvatiActivity extends AppCompatActivity {

    Toast toast;
    private ArrayList<flatData> flDatArrayList = new ArrayList<flatData>();
    dbClass db = new dbClass(this);
    private String lnk = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dohvati);

        Intent intent = getIntent();
        String message = intent.getStringExtra(glavnaActivity.EXTRA_MESSAGE);

        //this.lnk = "http://www.njuskalo.hr/index.php?ctl=search_ads&keywords=" + message + "&sort=new";
         this.lnk = message;

        toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        toast.show();

        napraviDohvat(this.lnk);

    }



    public void napraviDohvat(String upit) {

        try {

            String ctls = "search_ads";
            String sorts = "new";

            toast = Toast.makeText(this, "0", Toast.LENGTH_SHORT);
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

                            //Clean arraylist of apartments
                            flDatArrayList.clear();

                            parseRespIntoDatabase(msg);

                            //maybe another thread
                            for(int i=0; i<flDatArrayList.size(); i++)
                            {
                                db.addApartment(flDatArrayList.get(i));
                            }


                        }
                        catch(Exception ex)
                        {
                            //Log.d(ex.getMessage().toString());

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



        while(i >= 0) {

            i = resp.indexOf("data-ad-id", i+1);

            parseAllValues(resp.substring(n, i));
            n = i;


        }

    }


    public void parseAllValues(String valStr)
    {
        flatData flat = new flatData();

        String id, link, dtm, prize, description;

            id=substringBetween(valStr, "ad-id=\"","\"").trim();

            link = substringBetween(valStr, "\" class=\"link\" href=\"", "\">");
            link = "http://www.njuskalo.hr" + link;

            dtm = substringBetween(valStr,"datetime=\"","\" pubdate=").trim();
            prize = substringBetween(valStr,"price price--eur\">"," <span class=\"currency").trim();
            description = substringBetween(valStr,"<div class=\"entity-description-main\">","<br />").trim();

            flat.setId(id);
            flat.setDescription(description);
            flat.setDtm(dtm);
            flat.setLink(link);
            flat.setPrize(prize);

            //Add flat to array List
            flDatArrayList.add(flat);

            Log.d("ID", id);
            Log.d("LINK", link);
            Log.d("PRIZE", prize);
            Log.d("DESCRIPTION", description);
            Log.d("DTM", dtm);
            Log.d("NEWLINE", "-----------------------------------");

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.dohvatiLayout);

            TextView tv = new TextView(this);
            tv.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            String showStr = "ID: " + id + "\n" + "LINK: " + link + "\n" + "PRIZE: " + prize + "\n" + "DESC: " + description + "\n" + rebrandDate(dtm) + "\n\n";
            tv.setText(showStr);
            Linkify.addLinks(tv, Linkify.WEB_URLS);
            tv.setLinkTextColor(Color.parseColor("#2f6699"));
            linearLayout.addView(tv);


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


    public String rebrandDate(String dt)
    {
        String dtm;

        //2016-09-28T15:38:10+02:00
        String dtmSpl[] = dt.split("T");
        String dtSplTm[] = dtmSpl[1].split("+");
        dtm = dtmSpl + ", " + dtSplTm[0];

        return (dtm);
    }







}



