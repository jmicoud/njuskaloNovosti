package com.dev.stdev.njuskalonovosti;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;


public class brisiPretraguServis extends IntentService {

    private dbClass db = new dbClass(this);
    private List<pretrageClass> pretrageLista;

    public brisiPretraguServis() {
        super("brisiPretraguServis");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String pretraga = intent.getStringExtra(glavnaActivity.MESSAGE_BS);

            List<pretrageClass> prc = db.getPretragaByPretraga(pretraga);//only one element will be in list

            db.deletePretraga(pretraga); //delete pretraga
            db.deleteApartment(prc.get(0).getGeneralId()); //delete apartment

            //Log.d("U servisu dohvati","U servisu dohvati");

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
