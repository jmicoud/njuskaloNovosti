package com.dev.stdev.njuskalonovosti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class listaAlarmaActivity extends AppCompatActivity {

    private bReceiver bRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alarma);

        Intent intent = getIntent();
        String action = intent.getAction();
        //String message = intent.getStringExtra(glavnaActivity.MESSAGE_GA);

        if (action.equalsIgnoreCase(glavnaActivity.MESSAGE_STRA)) //stvori novi alarm
        {
            //stvori novi alarm
            String messa = intent.getStringExtra(glavnaActivity.MESSAGE_STRA);
            sendBroadcastMessage(glavnaActivity.MESSAGE_PNA, messa);

        }
        else if (action.equalsIgnoreCase(glavnaActivity.MESSAGE_GA)) //prikazi alarme
        {
            sendBroadcastMessage(glavnaActivity.MESSAGE_PNA,glavnaActivity.MESSAGE_GAL);

        }

        //Register receiver from service
        bRec = new bReceiver();
        IntentFilter filter = new IntentFilter("ALARM_BRD");
        registerReceiver(bRec, filter);

        //send intent to get alarm list to alarmiServis
        //sendBroadcastMessage(glavnaActivity.MESSAGE_GAL,glavnaActivity.MESSAGE_GAL);

        //Start ALARM START Service
        //Intent srva = new Intent(this, alarmiServis.class);
        //srva.putExtra("POKRENIALARME",MESSAGE_PA);
        //startService((srva));

    }


    private class bReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //if(intent.getStringExtra("RETSTRNG").equals("")) {
            //    String serviceJsonString = intent.getStringExtra("RETSTRING");

            String action = intent.getAction();

            if (action.equalsIgnoreCase(glavnaActivity.MESSAGE_RGAL)) //vratio se alarm za prikaz serijaliziran
            {

                    alarmClass ald = (alarmClass) intent.getSerializableExtra("ALARM_OBJECT");


                    //Do something with the string

                    Log.d("GENERALID", ald.getGeneralid());
                    Log.d("INTERVAL", ald.getInterval());
                    Log.d("PRETRAGA", ald.getPretraga());

                    Log.d("NEWLINE", "-----------------------------------");

                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.alarmLayout);

                    TextView tv = new TextView(getApplicationContext());

                    final Button myButton = new Button(getApplicationContext());
                    myButton.setText("Zaustavi");
                    int btId = Integer.parseInt(ald.getGeneralid());
                    myButton.setId(btId);
                    myButton.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    myButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //button id is id of alarm
                            sendBroadcastMessage(glavnaActivity.MESSAGE_STPA, Integer.toString(myButton.getId()));
                        }
                    });

                    tv.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    String showStr = "GENID: " + ald.getGeneralid() + "\n" + "INTERVAL: " + ald.getInterval() + "\n" + "PRETRAGA: " + ald.getPretraga() + "\n\n";
                    tv.setText(showStr);

                    linearLayout.addView(tv);
                    linearLayout.addView(myButton);
          }

            // }
            //}
        }
    }


    private void sendBroadcastMessage(String intentFilterName, String s) {

        //Log.d("Šaljem Intent","Šaljem Intent");

        Intent intent = new Intent(intentFilterName);
        intent.putExtra(intentFilterName, s);
        sendBroadcast(intent);
    }


    private void zaustavi()
    {



    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bRec);
    }

}
