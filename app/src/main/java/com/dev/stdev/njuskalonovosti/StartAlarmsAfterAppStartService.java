package com.dev.stdev.njuskalonovosti;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;


public class StartAlarmsAfterAppStartService extends IntentService {

    private DatabaseClass db = new DatabaseClass(this);

    public StartAlarmsAfterAppStartService() {
        super("StartAlarmsAfterAppStartService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            pokreniAlarme();
        }
    }



    public void pokreniAlarme()
    {

        List<AlarmClass> al = db.getAllAlarms();

        //if(al.size()>0) //only if there are alarms start service or start service anyway for future creation of services during app run
        //{
            startService(new Intent(getBaseContext(), AlarmConfigurationService.class));
        //}

        for(int i=0; i<al.size(); i++)
        {

            AlarmClass alr = al.get(i);
            long intrvl = SystemClock.elapsedRealtime() + 1000 * Integer.parseInt(alr.getInterval()); //interval is in seconds in database but alarm demands miliseconds
            int alarmid = Integer.parseInt(alr.getGeneralid());

            Log.d("ALARM ",Integer.toString(alarmid) + ", " + (1000 * Integer.parseInt(alr.getInterval())));

            //sendBroadcastMessage("ALARMREC","ALARMREC");

            //Calendar cal = Calendar.getInstance();
            //cal.add(Calendar.SECOND, 10);

            Intent intent = new Intent(this, AlarmConfigurationService.class);
            intent.putExtra(MainActivity.MESSAGE_ALARM, Integer.toString(alarmid));
            PendingIntent pintent = PendingIntent.getService(this, alarmid, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (Integer.parseInt(alr.getInterval()) * 1000), pintent);

        }

    }



    private void sendBroadcastMessage(String intentFilterName, String f) {

        //Log.d("Šaljem Intent","Šaljem Intent");

        Intent intent = new Intent(intentFilterName);
        intent.putExtra(MainActivity.MESSAGE_ALARM, f);
        sendBroadcast(intent);
    }




}
