package com.dev.stdev.njuskalonovosti;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.List;


public class alarmiServis extends IntentService {

    private dbClass db = new dbClass(this);
    private List<alarmClass> alarmiLista;

    public alarmiServis() {
        super("alarmiServis");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String action=intent.getAction();

            if(action.equalsIgnoreCase(glavnaActivity.MESSAGE_PA)) //pokreni postojeće alarme nakon pokretanja aplikacije
            {
               //pokreni alarme nakon starta
                pokreniAlarme();
            }
            else if(action.equalsIgnoreCase(glavnaActivity.MESSAGE_PNA)) //pokreni novi alarm
            {
               //pokreni novi alarm

            }
            else if(action.equalsIgnoreCase(glavnaActivity.MESSAGE_GAL)) //prikaži alarme
            {

                //prikazi alarme
               prikazialarme();
            }
            else if(action.equalsIgnoreCase(glavnaActivity.MESSAGE_STA)) //zaustavi alarm
            {

                //prikazi alarme
                prikazialarme();
            }
            //String pokal = intent.getStringExtra(glavnaActivity.MESSAGE_PA);

        }
    }


    public void pokreniAlarme()
    {

        List<alarmClass> al = db.getAllAlarms();

        for(int i=0; i<al.size(); i++)
        {

            alarmClass alr = al.get(i);
            int intrvl = 1000 * Integer.parseInt(alr.getInterval()); //interval is in seconds in database but alarm demands miliseconds
            int alarmid = Integer.parseInt(alr.getAlarmid());

            Intent intent = new Intent(this, alarmReceiver.class);
            PendingIntent pi = PendingIntent.getActivity(this, alarmid, intent, 0);
            AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, intrvl ,pi);

        }

    }


    public void prikazialarme()
    {

        alarmiLista = db.getAllAlarms();

        for(int i=0; i<alarmiLista.size(); i++)
        {
            alarmClass al = alarmiLista.get(i);
            sendBroadcastMessage(glavnaActivity.MESSAGE_RGAL, al);
        }

    }

    private void sendBroadcastMessage(String intentFilterName, alarmClass al) {

        //Log.d("Šaljem Intent","Šaljem Intent");

        Intent intent = new Intent(intentFilterName);
        intent.putExtra("ALARM_OBJECT", al);
        sendBroadcast(intent);
    }


}
