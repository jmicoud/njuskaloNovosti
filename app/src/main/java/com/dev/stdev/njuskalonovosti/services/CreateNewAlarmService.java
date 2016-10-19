package com.dev.stdev.njuskalonovosti.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dev.stdev.njuskalonovosti.classes.AlarmClass;
import com.dev.stdev.njuskalonovosti.activities.MainActivity;
import com.dev.stdev.njuskalonovosti.classes.SearchClass;
import com.dev.stdev.njuskalonovosti.database.DatabaseClass;

import java.util.List;


public class CreateNewAlarmService extends IntentService {

    private DatabaseClass db = new DatabaseClass(this);
    private List<AlarmClass> alarmList;

    public CreateNewAlarmService() {
        super("CreateNewAlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String alDat = intent.getStringExtra(MainActivity.MESSAGE_PNA);
            startNewAlarm(alDat);

        }
    }


    public void startNewAlarm(String alarmdata)
    {

        String[] parts = alarmdata.split("##");
        AlarmClass alc = new AlarmClass();
        SearchClass ptr = new SearchClass();

        String pretraga = parts[0];
        String intervl = parts[1];
        String tipPretrage = "1";

        alc.setInterval(intervl);
        ptr.setSearch(pretraga);
        ptr.setType(tipPretrage);

        int newGeneralId;

        if(!db.isPretrageTableEmpty())//==false)
        {


            List<SearchClass> lP = db.getAllSearch();

            SearchClass prTm;

            prTm = lP.get(lP.size()-1);//take last an generate +1 id for new one
            newGeneralId = Integer.parseInt(prTm.getGeneralId()) + 1; //zadnji plus 1 je id za novu pretragu

            ptr.setGeneralId(Integer.toString(newGeneralId));
            db.addSearch(ptr);

            alc.setGeneralid(Integer.toString(newGeneralId));
            db.addAlarm(alc);

            //dodaj novu pretragu - geerirajnovi general id, pretraži stanove na temelju novog id-a, nema ih naravno jer je nova pretraga, ddaj fld.isnew..



        }
        else //Pretrage table is empty
        {
            newGeneralId = 1000; //set initial generalid to 1000

            //getFlatsAdvertisments zadnju pretragu jer ima

            ptr.setGeneralId(Integer.toString(newGeneralId));
            db.addSearch(ptr);

            alc.setGeneralid(Integer.toString(newGeneralId));
            db.addAlarm(alc);

        }

        //create new alarm


        Intent intent = new Intent(this, AlarmConfigurationService.class);
        intent.putExtra(MainActivity.MESSAGE_ALARM, Integer.toString(newGeneralId));
        PendingIntent pintent = PendingIntent.getService(this, newGeneralId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (Integer.parseInt(intervl) * 1000), pintent);

        //---------------show new alarm/refresh alarm list in listaAlarmaactivity---------------
        showAlarms();
        //-------------------------------------------------------------------

    }

    public void showAlarms()
    {

        alarmList = db.getAllAlarms();

        for(int i = 0; i< alarmList.size(); i++)
        {
            AlarmClass al = alarmList.get(i);
            List<SearchClass> p = db.getSearchByGenID(al.getGeneralid()); //only one in list
            al.setSearch(p.get(0).getSearch()); //we are doing this so that pretraga string can be shown in activity
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
