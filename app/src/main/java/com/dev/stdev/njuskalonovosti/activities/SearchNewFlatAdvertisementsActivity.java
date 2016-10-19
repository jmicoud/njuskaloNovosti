package com.dev.stdev.njuskalonovosti.activities;

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

import com.dev.stdev.njuskalonovosti.R;
import com.dev.stdev.njuskalonovosti.classes.FlatAdvertismentClass;
import com.dev.stdev.njuskalonovosti.services.SearchNewFlatAdvertisementsService;

public class SearchNewFlatAdvertisementsActivity extends AppCompatActivity {


    private bReceiver bRec;
    Intent srvc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getflats);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE_GD);


        //Register receiver from service
        bRec = new bReceiver();
        IntentFilter filter = new IntentFilter("FLAT_BRD");
        registerReceiver(bRec,filter);


        srvc = new Intent(this, SearchNewFlatAdvertisementsService.class);
        srvc.putExtra("LINK",message);
        startService((srvc));


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

                FlatAdvertismentClass flD = (FlatAdvertismentClass) intent.getSerializableExtra("FLAT_OBJECT");


                //Log.d("DTM", flD.getDtm());
                //Log.d("ISNEW", flD.getIsNewFlat());
                //Log.d("NEWLINE", "-----------------------------------");

                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.getLayout);

                TextView tv = new TextView(SearchNewFlatAdvertisementsActivity.this);

                if(flD.getIsNewFlat().equals("1")) //new flats are in light green color
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


        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bRec);
    }


}



