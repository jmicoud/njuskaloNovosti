package com.dev.stdev.njuskalonovosti.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import com.dev.stdev.njuskalonovosti.activities.MainActivity;
import com.dev.stdev.njuskalonovosti.database.DatabaseClass;
import com.dev.stdev.njuskalonovosti.database.TableAlarm;
import com.dev.stdev.njuskalonovosti.database.TableFlat;
import com.dev.stdev.njuskalonovosti.database.TableSearch;
import com.dev.stdev.njuskalonovosti.models.AlarmClass;
import com.dev.stdev.njuskalonovosti.models.SearchClass;

import java.util.List;


public class FinishAlarmService extends IntentService {

    TableSearch tableSearch = new TableSearch(DatabaseClass.getDatabase());
    TableFlat tableFlat = new TableFlat(DatabaseClass.getDatabase());
    TableAlarm tableAlarm = new TableAlarm(DatabaseClass.getDatabase());

    public FinishAlarmService() {
        super("zavrsiAlarmServis");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            //zaustavi alarm
            String ala = intent.getStringExtra(MainActivity.MESSAGE_STPA);
            stopAlarm(ala);

        }
    }


    public void stopAlarm(String al)
    {

        //delete alarm and its dependencies from database
        tableAlarm.deleteAlarm(al);
        tableFlat.deleteFlat(al);
        tableSearch.deleteSearchByGenId(al);

        int alarmidn = Integer.parseInt(al);

        Intent intent = new Intent(this, AlarmConfigurationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, alarmidn, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        //---------------refresh alarm list in listaAlarmaactivity---------------
        showAlarms();
        //-------------------------------------------------------------------
    }


    public void showAlarms()
    {

        List<AlarmClass> alarmList = tableAlarm.getAllAlarms();

        for(int i = 0; i< alarmList.size(); i++)
        {
            AlarmClass al = alarmList.get(i);
            List<SearchClass> p = tableSearch.getSearchByGenID(al.getGeneralid()); //only one in list
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
