package com.mobile.ex5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    protected void AlerteBox()
    {
        AlertDialog.Builder _alert = new AlertDialog.Builder(MainActivity.this);
        _alert.setMessage(R.string.confirmationUserInfoMsg);
        _alert.setCancelable(true);

        _alert.setPositiveButton(R.string.validateButton, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        _alert.setNegativeButton(R.string.backButton, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        _alert.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.validateButton)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlerteBox();

            }
        });
    }
}