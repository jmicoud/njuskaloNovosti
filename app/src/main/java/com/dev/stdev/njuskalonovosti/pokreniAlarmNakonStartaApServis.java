package com.dev.stdev.njuskalonovosti;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class pokreniAlarmNakonStartaApServis extends IntentService {

    private dbClass db = new dbClass(this);

    public pokreniAlarmNakonStartaApServis() {
        super("pokreniAlarmNakonStartaApServis");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            pokreniAlarme();
        }
    }


    public void pokreniAlarme()
    {

        List<alarmClass> al = db.getAllAlarms();

        for(int i=0; i<al.size(); i++)
        {

            alarmClass alr = al.get(i);
            long intrvl = SystemClock.elapsedRealtime() + 1000 * Integer.parseInt(alr.getInterval()); //interval is in seconds in database but alarm demands miliseconds
            int alarmid = Integer.parseInt(alr.getGeneralid());

            Log.d("ALARM ",Integer.toString(alarmid) + ", " + intrvl);

            Intent intent = new Intent(this, alarmReceiver.class);
            intent.putExtra(glavnaActivity.MESSAGE_ALARM, Integer.toString(alarmid));
            PendingIntent pi = PendingIntent.getActivity(this, alarmid, intent, 0);
            AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
            //am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, intrvl ,pi);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000 * Integer.parseInt(alr.getInterval()), (Integer.parseInt(alr.getInterval()) * 1000) ,pi);


        }

    }







}
