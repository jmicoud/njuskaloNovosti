package com.dev.stdev.njuskalonovosti;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;


public class AlarmListService extends IntentService {

    private DatabaseClass db = new DatabaseClass(this);
    private List<AlarmClass> alarmiLista;

    public AlarmListService() {
        super("AlarmListService");
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
