package com.dev.stdev.njuskalonovosti.services;

import android.app.IntentService;
import android.content.Intent;

import com.dev.stdev.njuskalonovosti.activities.MainActivity;
import com.dev.stdev.njuskalonovosti.database.DatabaseClass;
import com.dev.stdev.njuskalonovosti.database.TableFlat;
import com.dev.stdev.njuskalonovosti.database.TableSearch;
import com.dev.stdev.njuskalonovosti.models.SearchClass;

import java.util.List;


public class DeleteSearchService extends IntentService {

    TableSearch tableSearch = new TableSearch(DatabaseClass.getDatabase());
    TableFlat tableFlat = new TableFlat(DatabaseClass.getDatabase());

    public DeleteSearchService() {
        super("DeleteSearchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String search = intent.getStringExtra(MainActivity.MESSAGE_BS);

            List<SearchClass> prc = tableSearch.getSearchBySearch(search);//only one element will be in list

            tableSearch.deleteSearch(search); //delete pretraga
            tableFlat.deleteFlat(prc.get(0).getGeneralId()); //delete apartment

            //Log.d("U servisu getFlatsAdvertisments","U servisu getFlatsAdvertisments");

            List<SearchClass> searchList = tableSearch.getAllSearch();

            for(int i = 0; i< searchList.size(); i++)
            {
                SearchClass pr = searchList.get(i);
                sendBroadcastMessage("PRETRAGE_RESP", pr);

            }



        }
    }

    private void sendBroadcastMessage(String intentFilterName, SearchClass f) {

        //Log.d("Šaljem Intent","Šaljem Intent");

        Intent intent = new Intent(intentFilterName);
        intent.putExtra("PRETRAGA_OBJ", f);
        sendBroadcast(intent);
    }


}
