package com.dev.stdev.njuskalonovosti;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.List;


public class zavrsiAlarmServis extends IntentService {

    private dbClass db = new dbClass(this);
    private List<alarmClass> alarmiLista;

    public zavrsiAlarmServis() {
        super("zavrsiAlarmServis");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            //zaustavi alarm
            String ala = intent.getStringExtra(glavnaActivity.MESSAGE_STPA);
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

        Intent intent = new Intent(this, alarmServis.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmidn, intent, 0);
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
            alarmClass al = alarmiLista.get(i);
            List<pretrageClass> p = db.getPretragaByGenID(al.getGeneralid()); //only one in list
            al.setPretraga(p.get(0).getPretraga()); //we are doing this so that pretraga string can be shown in activity
            sendBroadcastMessage(glavnaActivity.MESSAGE_RGAL, al);
        }

    }


    private void sendBroadcastMessage(String intentFilterName, alarmClass al) {

        //Log.d("Šaljem Intent","Šaljem Intent");

        Intent intent = new Intent(intentFilterName);
        intent.setAction(intentFilterName);
        intent.putExtra(intentFilterName, al);
        sendBroadcast(intent);
    }


}
