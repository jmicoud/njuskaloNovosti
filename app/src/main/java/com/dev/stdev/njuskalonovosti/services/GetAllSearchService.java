package com.dev.stdev.njuskalonovosti.services;

import android.app.IntentService;
import android.content.Intent;

import com.dev.stdev.njuskalonovosti.classes.SearchClass;
import com.dev.stdev.njuskalonovosti.database.DatabaseClass;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetAllSearchService extends IntentService {


    private DatabaseClass db = new DatabaseClass(this);
    private List<SearchClass> searchList;

    public GetAllSearchService() {
        super("GetAllSearchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            //Log.d("U servisu getFlatsAdvertisments","U servisu getFlatsAdvertisments");

            searchList = db.getAllSearch();

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
