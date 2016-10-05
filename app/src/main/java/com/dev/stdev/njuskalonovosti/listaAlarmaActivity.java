package com.dev.stdev.njuskalonovosti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class listaAlarmaActivity extends AppCompatActivity {

    private bReceiver bRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alarma);

        Intent intent = getIntent();
        String message = intent.getStringExtra(glavnaActivity.MESSAGE_GA);

        //Register receiver from service
        bRec = new bReceiver();
        IntentFilter filter = new IntentFilter("ALARM_BRD");
        registerReceiver(bRec, filter);

        if(message.equals("DOHVATI_LISTU_ALARMA")) //samo dohvati listu alarma i prikaži
        {

            //Start Service
            Intent srvc = new Intent(this, dohvatiListuAlarmaServis.class);
            srvc.putExtra("GETALARMLIST", message);
            startService((srvc));

        }
        else //set and start alarm
        {

            //Start Service
            Intent srvc = new Intent(this, dohvatiListuAlarmaServis.class);
            srvc.putExtra("SETALARM", message);
            startService((srvc));
        }

    }


    private class bReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //if(intent.getStringExtra("RETSTRNG").equals("")) {
            //    String serviceJsonString = intent.getStringExtra("RETSTRING");



            flatData flD = (flatData) intent.getSerializableExtra("ALARM_OBJECT");



            //Do something with the string


           /* Log.d("ID", flD.getId());
            Log.d("LINK", flD.getLink());
            Log.d("PRIZE", flD.getPrize());
            Log.d("DESCRIPTION", flD.getDescription());
            Log.d("DTM", flD.getDtm());
            Log.d("NEWLINE", "-----------------------------------");*/

           /* LinearLayout linearLayout = (LinearLayout) findViewById(R.id.dohvatiLayout);

            TextView tv = new TextView(getApplicationContext());

            if(flD.getIsNewApartment().equals("1")) //new flats are in light green color
            {
                tv.setBackgroundColor(Color.parseColor("#90EE90"));
            }

            tv.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            String showStr = "ID: " + flD.getId() + "\n" + "LINK: " + flD.getLink() + "\n" + "PRIZE: " + flD.getPrize() + "\n" + "DESC: " + flD.getDescription() + "\n" + rebrandDate(flD.getDtm()) + "\n\n";
            tv.setText(showStr);
            Linkify.addLinks(tv, Linkify.WEB_URLS);
            tv.setLinkTextColor(Color.parseColor("#2f6699"));
            linearLayout.addView(tv);*/


            // }
            //}
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bRec);
    }

}
