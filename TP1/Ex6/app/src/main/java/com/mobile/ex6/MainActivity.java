package com.mobile.ex6;

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
    protected void PutExtraIntent(Intent _intent, int _idRes)
    {
        EditText _editField = (EditText)findViewById(_idRes);
        _intent.putExtra(Integer.toString(_idRes), _editField.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.validateButton)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(MainActivity.this, Confirmation.class);
                PutExtraIntent(_intent, R.id.nameEdit);
                PutExtraIntent(_intent, R.id.firstNameEdit);
                PutExtraIntent(_intent, R.id.ageEdit);
                PutExtraIntent(_intent, R.id.expAreaEdit);
                PutExtraIntent(_intent, R.id.telephoneEdit);
                startActivity(_intent);
            }
        });
    }
}