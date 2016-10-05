package com.dev.stdev.njuskalonovosti;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class dohvatiListuAlarmaServis extends IntentService {


    public dohvatiListuAlarmaServis() {
        super("dohvatiListuAlarmaServis");
    }
    private dbClass db = new dbClass(this);


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String code = intent.getStringExtra("MESSAGE");

            if(code.equals("GETALARMLIST"))
            {

                List<pretrageClass> prtg = db.getAllPretrage();

            }
            else if(code.equals("SETALARM"))
            {


            }


        }
    }


}
