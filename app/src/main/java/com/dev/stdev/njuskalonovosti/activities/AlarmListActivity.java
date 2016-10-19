package com.dev.stdev.njuskalonovosti.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.stdev.njuskalonovosti.R;
import com.dev.stdev.njuskalonovosti.classes.AlarmClass;
import com.dev.stdev.njuskalonovosti.services.AlarmListService;
import com.dev.stdev.njuskalonovosti.services.CreateNewAlarmService;
import com.dev.stdev.njuskalonovosti.services.FinishAlarmService;

public class AlarmListActivity extends AppCompatActivity {

    private bReceiver bRec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        Intent intent = getIntent();

        if (intent != null) {

            String action = intent.getAction();
            //Log.d("ACTON",action);

            if (action.equalsIgnoreCase(MainActivity.MESSAGE_STRA)) //stvori novi alarm
            {
                //stvori novi alarm
                //Log.d("MESSAGE_STRA","Message_STRA");
                String messa = intent.getStringExtra(MainActivity.MESSAGE_STRA);
                Intent srvc = new Intent(this, CreateNewAlarmService.class);
                srvc.putExtra(MainActivity.MESSAGE_PNA,messa);
                startService((srvc));

            } else if (action.equalsIgnoreCase(MainActivity.MESSAGE_GA)) //prikazi alarme
            {
                Intent srvc = new Intent(this, AlarmListService.class);
                srvc.putExtra(MainActivity.MESSAGE_GAL, MainActivity.MESSAGE_GAL);
                startService((srvc));

            }

            //Register receiver from service
            bRec = new bReceiver();
            IntentFilter filter = new IntentFilter(MainActivity.MESSAGE_RGAL);
            registerReceiver(bRec, filter);

        }
    }


    private class bReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


                    AlarmClass ald = (AlarmClass) intent.getSerializableExtra(MainActivity.MESSAGE_RGAL);

                    //Do something with the string

                    //Log.d("GENERALID", ald.getGeneralid());
                    //Log.d("INTERVAL", ald.getInterval());
                    //Log.d("PRETRAGA", ald.getSearch());

                    //Log.d("NEWLINE", "-----------------------------------");

                    final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.alarmLayout);

                    TextView tv = new TextView(getApplicationContext());

                    final Button myButton = new Button(getApplicationContext());
                    myButton.setText("Zaustavi");
                    int btId = Integer.parseInt(ald.getGeneralid());
                    myButton.setId(btId);
                    myButton.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    //myButton.setRight(10);

                    myButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //button id is id of alarm

                            linearLayout.removeAllViews();

                            Intent srvc = new Intent(getApplicationContext(), FinishAlarmService.class);
                            srvc.putExtra(MainActivity.MESSAGE_STPA,Integer.toString(myButton.getId()));
                            startService((srvc));
                        }
                    });

                    tv.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    String showStr = "GENID: " + ald.getGeneralid() + "\n" + "INTERVAL: " + ald.getInterval() + "\n" + "PRETRAGA: " + ald.getSearch() + "\n\n";
                    tv.setText(showStr);

                    linearLayout.addView(myButton);
                    linearLayout.addView(tv);

        }
    }


    private void sendBroadcastMessage(String intentFilterName, String s) {

        Intent intent = new Intent(intentFilterName);
        intent.setAction(intentFilterName);
        intent.putExtra(intentFilterName, s);
        sendBroadcast(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bRec);
    }

}
