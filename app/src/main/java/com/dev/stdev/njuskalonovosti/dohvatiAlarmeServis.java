package com.dev.stdev.njuskalonovosti;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;


public class dohvatiAlarmeServis extends IntentService {

    private dbClass db = new dbClass(this);
    private List<alarmClass> alarmiLista;

    public dohvatiAlarmeServis() {
        super("dohvatiAlarmeServis");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            //prikazi alarme
            prikazialarme();

        }
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
