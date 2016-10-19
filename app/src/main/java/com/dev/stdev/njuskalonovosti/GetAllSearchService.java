package com.dev.stdev.njuskalonovosti;

import android.app.IntentService;
import android.content.Intent;

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
    private List<SearchClass> pretrageLista;

    public GetAllSearchService() {
        super("GetAllSearchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            //Log.d("U servisu dohvati","U servisu dohvati");

            pretrageLista = db.getAllPretrage();

            for(int i=0; i<pretrageLista.size(); i++)
            {
                SearchClass pr = pretrageLista.get(i);
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
