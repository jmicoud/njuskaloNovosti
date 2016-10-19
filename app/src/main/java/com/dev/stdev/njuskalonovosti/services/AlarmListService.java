package com.dev.stdev.njuskalonovosti.services;

import android.app.IntentService;
import android.content.Intent;

import com.dev.stdev.njuskalonovosti.classes.AlarmClass;
import com.dev.stdev.njuskalonovosti.activities.MainActivity;
import com.dev.stdev.njuskalonovosti.classes.SearchClass;
import com.dev.stdev.njuskalonovosti.database.DatabaseClass;

import java.util.List;


public class AlarmListService extends IntentService {

    private DatabaseClass db = new DatabaseClass(this);
    private List<AlarmClass> alarmList;

    public AlarmListService() {
        super("AlarmListService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            //prikazi alarme
            showAlarms();

        }
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
