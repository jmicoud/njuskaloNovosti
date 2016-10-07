package com.dev.stdev.njuskalonovosti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class konfiguracijaActivity extends AppCompatActivity {

    //public final static String MESSAGE = "MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfiguracija);


        Intent intent = getIntent();
        String message = intent.getStringExtra(glavnaActivity.MESSAGE_GK);

        TextView tv = (TextView)findViewById(R.id.editKonfiguracija);
        tv.setText(message);

    }


    public void stvorialarm(View view) {
        // Do something in response to button

        EditText editText = (EditText) findViewById(R.id.editKonfiguracija);
        String message = editText.getText().toString();

        Spinner spinnerKonf=(Spinner) findViewById(R.id.spinnerKonfiguracija);
        String selitem = spinnerKonf.getSelectedItem().toString();

        String messageCo = message + "##" + selitem;

        //search string must not be empty
        if(message!="") {
            Intent intent = new Intent(this, listaAlarmaActivity.class);
            intent.setAction(glavnaActivity.MESSAGE_STRA);
            intent.putExtra(glavnaActivity.MESSAGE_STRA, messageCo);
            startActivity(intent);
        }

    }

}
