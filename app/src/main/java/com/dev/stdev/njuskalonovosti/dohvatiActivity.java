package com.dev.stdev.njuskalonovosti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            int a;

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
                            Log.d("RESPONSA", response.body().string());

                            String msg =  response.body().string();
                            //int lnt = msg.length();

                            parseRespIntoDatabase(msg);


                        }
                        catch(Exception ex)
                        {
                            //Log.d(ex.getMessage().toString());

                        }

                        toast.show();
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
            //Log.d("RESPONSE", response.message());
            toast = Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }



    public interface NjuskaloService {

        @GET("/index.php")
        Call<ResponseBody> getTask(@Query("ctl") String ctl, @Query("keywords") String keyw, @Query("sort") String srt);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.njuskalo.hr/")
                .build();

    }



    public int parseRespIntoDatabase(String resp)
    {

        //int locSt = resp.indexOf("Njuškalo oglasi");
        int i, n;

        i = resp.indexOf("data-ad-id");
        n = i;

        while(i >= 0) {
            //System.out.println(i);
            i = resp.indexOf("data-ad-id", i+1);
            parseAllValues(resp.substring(i, (n-i-1)));
            n = i;

        }

        return (1);

    }


    public void parseAllValues(String valStr)
    {
        flatData flat = new flatData();

        String id;


        for(int i=0; i<valStr.length(); i++)
        {

           if((valStr.substring(i,1).equals("n")) && (valStr.substring(i+1,1).equals("a")) && ((valStr.substring(i+2,1).equals("m")) && (valStr.substring(i+3,1).equals("e"))))
           {

               id=valStr.substring(i+5,1) + valStr.substring(i+6,1) + valStr.substring(i+7,1) + valStr.substring(i+8,1) + valStr.substring(i+9,1) + valStr.substring(i+10,1) + valStr.substring(i+11,1) + valStr.substring(i+12,1);

           }

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



    }



