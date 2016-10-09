package com.dev.stdev.njuskalonovosti;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;
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

        //if(al.size()>0) //only if there are alarms start service or start service anyway for future creation of services during app run
        //{
            startService(new Intent(getBaseContext(), alarmServis.class));
        //}

        for(int i=0; i<al.size(); i++)
        {

            alarmClass alr = al.get(i);
            long intrvl = SystemClock.elapsedRealtime() + 1000 * Integer.parseInt(alr.getInterval()); //interval is in seconds in database but alarm demands miliseconds
            int alarmid = Integer.parseInt(alr.getGeneralid());

            Log.d("ALARM ",Integer.toString(alarmid) + ", " + (1000 * Integer.parseInt(alr.getInterval())));

            //sendBroadcastMessage("ALARMREC","ALARMREC");

            //Calendar cal = Calendar.getInstance();
            //cal.add(Calendar.SECOND, 10);

            Intent intent = new Intent(this, alarmServis.class);
            intent.putExtra(glavnaActivity.MESSAGE_ALARM, Integer.toString(alarmid));
            PendingIntent pintent = PendingIntent.getService(this, alarmid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (Integer.parseInt(alr.getInterval()) * 1000), pintent);

        }

    }



    private void sendBroadcastMessage(String intentFilterName, String f) {

        //Log.d("Šaljem Intent","Šaljem Intent");

        Intent intent = new Intent(intentFilterName);
        intent.putExtra(glavnaActivity.MESSAGE_ALARM, f);
        sendBroadcast(intent);
    }




}
