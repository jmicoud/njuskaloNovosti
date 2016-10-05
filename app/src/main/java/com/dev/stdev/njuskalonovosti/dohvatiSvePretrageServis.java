package com.dev.stdev.njuskalonovosti;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class dohvatiSvePretrageServis extends IntentService {


    private dbClass db = new dbClass(this);
    private List<pretrageClass> pretrageLista;

    public dohvatiSvePretrageServis() {
        super("dohvatiSvePretrageServis");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            Log.d("U servisu dohvati","U servisu dohvati");

            pretrageLista = db.getAllPretrage();

            for(int i=0; i<pretrageLista.size(); i++)
            {
                pretrageClass pr = pretrageLista.get(i);
                sendBroadcastMessage("PRETRAGE_RESP", pr);

            }



        }
    }


    private void sendBroadcastMessage(String intentFilterName, pretrageClass f) {

        //Log.d("Šaljem Intent","Šaljem Intent");

        Intent intent = new Intent(intentFilterName);
        intent.putExtra("PRETRAGA_OBJ", f);
        sendBroadcast(intent);
    }


}
