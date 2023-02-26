package com.mobile.ex7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

public class EndActivity extends AppCompatActivity {

    protected void FillFieldWithExtra(Intent _intent, int _idExtra)
    {
        String _value = _intent.getStringExtra(Integer.toString(_idExtra));
        TextView _fieldToFill = (TextView)findViewById(_idExtra);
        _fieldToFill.setText(_value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        Intent _intent = getIntent();
        FillFieldWithExtra(_intent, R.id.telephoneEdit);

        ((ImageButton)findViewById(R.id.backMenu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(EndActivity.this, MainActivity.class);
                startActivity(_intent);
            }
        });

        ((ImageButton)findViewById(R.id.call)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _telNum = _intent.getStringExtra(Integer.toString(R.id.telephoneEdit));
                Intent _intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + _telNum));
                startActivity(_intent);
            }
        });

        ((Chip)findViewById(R.id.chip_call)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _telNum = _intent.getStringExtra(Integer.toString(R.id.telephoneEdit));
                Intent _intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + _telNum));
                startActivity(_intent);
            }
        });
    }
}