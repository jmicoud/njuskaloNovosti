package com.dev.stdev.njuskalonovosti;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String MESSAGE_GD = "MESSAGE_GD";
    public final static String MESSAGE_GK = "MESSAGE_GK";
    public final static String MESSAGE_GS = "MESSAGE_GS";
    public final static String MESSAGE_GA = "MESSAGE_GA";
    public final static String MESSAGE_GAL = "MESSAGE_GAL";
    public final static String MESSAGE_RGAL = "MESSAGE_RGAL";
    public final static String MESSAGE_BS = "MESSAGE_BS";
    public final static String MESSAGE_PA = "MESSAGE_PA";
    public final static String MESSAGE_PNA = "MESSAGE_PNA";
    public final static String MESSAGE_STRA = "MESSAGE_STRA";
    public final static String MESSAGE_STPA = "MESSAGE_STPA";
    public final static String MESSAGE_ALARM = "MESSAGE_ALARM";

    private bReceiver bRec;

    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glavna);

        //Intent intent = getIntent();
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
    //}


        //Log.d("Prije receivera","prije receivera");
        //Register receiver from service
        bRec = new MainActivity.bReceiver();
        IntentFilter filter = new IntentFilter("PRETRAGE_RESP");
        registerReceiver(bRec,filter);



        //Start GET ALL SEARCHES Service
        Intent srvc = new Intent(this, GetAllSearchService.class);
        srvc.putExtra("PRETRAGA",MESSAGE_GS);
        startService((srvc));



        //Start ALARM START Service
        Intent srva = new Intent(this, StartAlarmsAfterAppStartService.class);
        //srva.setAction("ja");
        //srva.putExtra("ju","ju");
        startService((srva));
        //Log.d("Poslije Alarm servisa","poslije Alarm servisa");

        //------------------spinner select value------------------------------------
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                TextView tv = (TextView) findViewById(R.id.edit_message);
                tv.setText(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
        //--------------------------------------------------------------------------------



    }



    //private BroadcastReceiver bReceiver = new BroadcastReceiver() {
   private class bReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //if(intent.getStringExtra("RETSTRNG").equals("")) {
            //    String serviceJsonString = intent.getStringExtra("RETSTRING");


            //Log.d("TU SAM U Receiveru","evo me");

            //if (intent.get("FLAT_BRD")!=null) {
            //Bundle bundle = intent.getExtras();

            SearchClass prt = (SearchClass) intent.getSerializableExtra("PRETRAGA_OBJ");

            //FlatAdvertismentClass flD = (FlatAdvertismentClass) bundle.getSerializable("FLAT_OBJECT");

            //Do something with the string


            //Log.d("ID", prt.getGeneralId());
            //Log.d("PRETRAGA", prt.getPretraga());
            //Log.d("TIP", prt.getTip());
            //Log.d("NEWLINE", "-----------------------------------");

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.glavnaLayout);

            Spinner sp = (Spinner) findViewById(R.id.spinner);

            //not alarm pretraga, 0-not alarm, 1 yes alarm
            if(prt.getTip().equals("0")) {

                ArrayAdapter<String> adapter;

                list.add(prt.getPretraga()); //prt.getGeneralId()+","+prt.getPretraga()
                adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp.setAdapter(adapter);
            }

        }
    }




    /** Called when the user clicks the Dohvati button */
    public void dohvati(View view) {
        // Do something in response to button

        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        //Log.d("PORUKALINK",message);
        //search string must not be empty
        if(message!="") {
            Intent intent = new Intent(this, SearchNewFlatAdvertisementsActivity.class);
            intent.putExtra(MESSAGE_GD, message);
            startActivity(intent);
        }

    }


    public void brisipretragu(View view)
    {

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String text = spinner.getSelectedItem().toString(); //dohvati oznacenu pretragu

        if(text!="") {


            //isprazni listu
            Spinner sp = (Spinner) findViewById(R.id.spinner);
            ArrayAdapter<String> adapter;

            list.clear();; //prt.getGeneralId()+","+prt.getPretraga()
            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(adapter);

            //Start Service
            Intent srvc = new Intent(this, DeleteSearchService.class);
            srvc.putExtra(MESSAGE_BS, text);
            startService((srvc));
        }

    }



    public void konfiguriraj(View view) {
        // Do something in response to button

        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        //search string must not be empty
        if(message!="") {
            Intent intent = new Intent(this, AlarmConfigurationActivity.class);
            intent.putExtra(MESSAGE_GK, message);
            startActivity(intent);
        }

    }

    public void ListaAlarma(View view) {
        // Do something in response to button

        //String message = "DOHVATI_LISTU_ALARMA";

        //search string must not be empty
       // if(message!="") {
            Intent intent = new Intent(this, AlarmListActivity.class);
            intent.setAction(MESSAGE_GA);
            intent.putExtra(MESSAGE_GA, MESSAGE_GA);
            startActivity(intent);
        //}

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bRec);
    }



}
