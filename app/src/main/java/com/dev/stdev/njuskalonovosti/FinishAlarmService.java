package com.dev.stdev.njuskalonovosti;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.List;


public class FinishAlarmService extends IntentService {

    private DatabaseClass db = new DatabaseClass(this);
    private List<AlarmClass> alarmiLista;

    public FinishAlarmService() {
        super("zavrsiAlarmServis");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            //zaustavi alarm
            String ala = intent.getStringExtra(MainActivity.MESSAGE_STPA);
            zaustaviAlarm(ala);

        }
    }


    public void zaustaviAlarm(String al)
    {

        //delete alarm and its dependencies from database
        db.deleteAlarm(al);
        db.deleteApartment(al);
        db.deletePretragaByGenId(al);

        int alarmidn = Integer.parseInt(al);

        Intent intent = new Intent(this, AlarmConfigurationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, alarmidn, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        //---------------refresh alarm list in listaAlarmaactivity---------------
        prikazialarme();
        //-------------------------------------------------------------------
    }


    public void prikazialarme()
    {

        alarmiLista = db.getAllAlarms();

        for(int i=0; i<alarmiLista.size(); i++)
        {
            AlarmClass al = alarmiLista.get(i);
            List<SearchClass> p = db.getPretragaByGenID(al.getGeneralid()); //only one in list
            al.setPretraga(p.get(0).getPretraga()); //we are doing this so that pretraga string can be shown in activity
            sendBroadcastMessage(MainActivity.MESSAGE_RGAL, al);
        }

    }


    private void sendBroadcastMessage(String intentFilterName, AlarmClass al) {

        //Log.d("Šaljem Intent","Šaljem Intent");

        Intent intent = new Intent(intentFilterName);
        intent.setAction(intentFilterName);
        intent.putExtra(intentFilterName, al);
        sendBroadcast(intent);
    }


}
