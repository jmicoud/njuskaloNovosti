package com.dev.stdev.njuskalonovosti.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dev.stdev.njuskalonovosti.R;

public class AlarmConfigurationActivity extends AppCompatActivity {

    //public final static String MESSAGE = "MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);


        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE_GK);

        TextView tv = (TextView)findViewById(R.id.configurationEdit);
        tv.setText(message);

    }


    public void createAlarm(View view) {
        // Do something in response to button

        EditText editText = (EditText) findViewById(R.id.configurationEdit);
        String message = editText.getText().toString();

        Spinner spinnerKonf=(Spinner) findViewById(R.id.configurationSpinner);
        String selitem = spinnerKonf.getSelectedItem().toString();

        String messageCo = message + "##" + selitem;

        //search string must not be empty
        if(!(message.equals(""))) {
            Intent intent = new Intent(this, AlarmListActivity.class);
            intent.setAction(MainActivity.MESSAGE_STRA);
            intent.putExtra(MainActivity.MESSAGE_STRA, messageCo);
            startActivity(intent);
        }

    }

}
