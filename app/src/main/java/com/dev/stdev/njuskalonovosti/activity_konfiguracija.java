package com.dev.stdev.njuskalonovosti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class activity_konfiguracija extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfiguracija);


        Intent intent = getIntent();
        String message = intent.getStringExtra(glavnaActivity.EXTRA_MESSAGE);



    }
}
