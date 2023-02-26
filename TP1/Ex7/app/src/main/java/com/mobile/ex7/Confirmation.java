package com.mobile.ex7;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Confirmation extends AppCompatActivity {

    protected void FillFieldWithExtra(Intent _intent, int _idExtra)
    {
        String _value = _intent.getStringExtra(Integer.toString(_idExtra));
        TextView _fieldToFill = (TextView)findViewById(_idExtra);
        _fieldToFill.setText(_value);
    }

    protected void PutExtraIntent(Intent _intent, int _idRes)
    {
        TextView _editField = (TextView)findViewById(_idRes);
        _intent.putExtra(Integer.toString(_idRes), _editField.getText().toString());
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        Intent _currentIntent = getIntent();
        FillFieldWithExtra(_currentIntent, R.id.nameEdit);
        FillFieldWithExtra(_currentIntent, R.id.firstNameEdit);
        FillFieldWithExtra(_currentIntent, R.id.ageEdit);
        FillFieldWithExtra(_currentIntent, R.id.expAreaEdit);
        FillFieldWithExtra(_currentIntent, R.id.telephoneEdit);
        ((Button)findViewById(R.id.validateButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(Confirmation.this, EndActivity.class);
                PutExtraIntent(_intent, R.id.telephoneEdit);
                startActivity(_intent);
            }
        });

        ((Button)findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}