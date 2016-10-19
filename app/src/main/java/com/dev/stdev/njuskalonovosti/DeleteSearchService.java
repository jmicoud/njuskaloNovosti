package com.dev.stdev.njuskalonovosti;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;


public class DeleteSearchService extends IntentService {

    private DatabaseClass db = new DatabaseClass(this);
    private List<SearchClass> pretrageLista;

    public DeleteSearchService() {
        super("DeleteSearchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String pretraga = intent.getStringExtra(MainActivity.MESSAGE_BS);

            List<SearchClass> prc = db.getPretragaByPretraga(pretraga);//only one element will be in list

            db.deletePretraga(pretraga); //delete pretraga
            db.deleteApartment(prc.get(0).getGeneralId()); //delete apartment

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
