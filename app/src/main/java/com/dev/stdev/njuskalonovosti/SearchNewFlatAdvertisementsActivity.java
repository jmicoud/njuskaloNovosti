package com.dev.stdev.njuskalonovosti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.util.Linkify;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchNewFlatAdvertisementsActivity extends AppCompatActivity {

    //Toast toast;

    //public final static String MESSAGE_D = "MESSAGED";
    private bReceiver bRec;
    Intent srvc;

    //IntentFilter progressfilter = new IntentFilter("FLAT_BRD");
    //registerReceiver(bReceiver,progressfilter);


    //DatabaseClass db = new DatabaseClass(this);
    //private String lnk = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dohvati);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE_GD);

        //this.lnk = "http://www.njuskalo.hr/index.php?ctl=search_ads&keywords=" + message + "&sort=new";
        //this.lnk = message;

        //toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        //toast.show();

        //Register receiver from service
        bRec = new bReceiver();
        IntentFilter filter = new IntentFilter("FLAT_BRD");
        registerReceiver(bRec,filter);

        //Log.d("PRIJENOSDOH",message);

        //Start Service, du background job in service
        //Intent srvc = new Intent(this, SearchNewFlatAdvertisementsService.class);
        srvc = new Intent(this, SearchNewFlatAdvertisementsService.class);
        srvc.putExtra("LINK",message);
        startService((srvc));

        //Log.d("U servisu","SERVIS");

    }


    public String rebrandDate(String dt)
    {
        String dtm;

        //2016-09-28T15:38:10+02:00
        String dtmSpl[] = dt.split("T");
        dtm = dtmSpl[0] + ", " + dtmSpl[1];

        return (dtm);
    }



    //private BroadcastReceiver bReceiver = new BroadcastReceiver() {
    private class bReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //if(intent.getStringExtra("RETSTRNG").equals("")) {
            //    String serviceJsonString = intent.getStringExtra("RETSTRING");


            //Log.d("TU SAM U Receiveru","evo me");

            //if (intent.get("FLAT_BRD")!=null) {
                //Bundle bundle = intent.getExtras();

                FlatAdvertismentClass flD = (FlatAdvertismentClass) intent.getSerializableExtra("FLAT_OBJECT");

                //FlatAdvertismentClass flD = (FlatAdvertismentClass) bundle.getSerializable("FLAT_OBJECT");

                //Do something with the string


                //Log.d("ID", flD.getId());
                //Log.d("LINK", flD.getLink());
                //Log.d("PRIZE", flD.getPrize());
                //Log.d("DESCRIPTION", flD.getDescription());
                //Log.d("DTM", flD.getDtm());
                //Log.d("ISNEW", flD.getIsNewApartment());
                //Log.d("NEWLINE", "-----------------------------------");

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.dohvatiLayout);

                TextView tv = new TextView(SearchNewFlatAdvertisementsActivity.this);

                if(flD.getIsNewApartment().equals("1")) //new flats are in light green color
                {
                    tv.setBackgroundColor(Color.parseColor("#90EE90"));
                }

                tv.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                String showStr = "ID: " + flD.getId() + "\n" + "LINK: " + flD.getLink() + "\n" + "PRIZE: " + flD.getPrize() + "\n" + "DESC: " + flD.getDescription() + "\n" + rebrandDate(flD.getDtm()) + "\n\n";
                tv.setText(showStr);
                Linkify.addLinks(tv, Linkify.WEB_URLS);
                tv.setLinkTextColor(Color.parseColor("#2f6699"));
                linearLayout.addView(tv);

                //stop service after finish
                stopService(srvc);

                // }
            //}
        }
    }


    //IntentFilter progressfilter = new IntentFilter("FLAT_BRD");
    //registerReceiver(bReceiver,progressfilter);



    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bRec);
    }





}



