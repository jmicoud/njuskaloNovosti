package com.dev.stdev.njuskalonovosti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class konfiguracijaActivity extends AppCompatActivity {

    public final static String MESSAGE = "MESSAGE";

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

        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        //search string must not be empty
        if(message!="") {
            Intent intent = new Intent(this, listaAlarmaActivity.class);
            intent.putExtra(MESSAGE, message);
            startActivity(intent);
        }

    }

}
