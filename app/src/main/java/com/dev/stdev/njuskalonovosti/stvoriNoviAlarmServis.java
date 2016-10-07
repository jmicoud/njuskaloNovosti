package com.dev.stdev.njuskalonovosti;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.List;


public class stvoriNoviAlarmServis extends IntentService {

    private dbClass db = new dbClass(this);
    private List<alarmClass> alarmiLista;

    public stvoriNoviAlarmServis() {
        super("stvoriNoviAlarmServis");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String alDat = intent.getStringExtra(glavnaActivity.MESSAGE_PNA);
            pokreniNoviAlarm(alDat);

        }
    }


    public void pokreniNoviAlarm(String alarmdata)
    {

        String[] parts = alarmdata.split("##");
        alarmClass alc = new alarmClass();
        pretrageClass ptr = new pretrageClass();

        String pretraga = parts[0];
        String intervl = parts[1];
        String tipPretrage = "1";

        alc.setInterval(intervl);
        ptr.setPretraga(pretraga);
        ptr.setTip(tipPretrage);

        int newGeneralId;

        if(db.isPretrageTableEmpty()==false)
        {


            List<pretrageClass> lP = db.getAllPretrage();

            pretrageClass prTm;

            prTm = lP.get(lP.size()-1);//take last an generate +1 id for new one
            newGeneralId = Integer.parseInt(prTm.getGeneralId()) + 1; //zadnji plus 1 je id za novu pretragu

            ptr.setGeneralId(Integer.toString(newGeneralId));
            db.addPretraga(ptr);

            alc.setGeneralid(Integer.toString(newGeneralId));
            db.addAlarm(alc);

            //dodaj novu pretragu - geerirajnovi general id, pretraži stanove na temelju novog id-a, nema ih naravno jer je nova pretraga, ddaj fld.isnew..



        }
        else //Pretrage table is empty
        {
            newGeneralId = 1000; //set initial generalid to 1000

            //dohvati zadnju pretragu jer ima

            ptr.setGeneralId(Integer.toString(newGeneralId));
            db.addPretraga(ptr);

            alc.setGeneralid(Integer.toString(newGeneralId));
            db.addAlarm(alc);

        }

        //create new alarm


        Intent intent = new Intent(this, alarmServis.class);
        intent.putExtra(glavnaActivity.MESSAGE_ALARM, Integer.toString(newGeneralId));
        PendingIntent pintent = PendingIntent.getService(this, newGeneralId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (Integer.parseInt(intervl) * 1000), pintent);

        //---------------show new alarm/refresh alarm list in listaAlarmaactivity---------------
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
