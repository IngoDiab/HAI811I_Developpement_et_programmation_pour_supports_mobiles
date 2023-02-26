package com.mobile.ex8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Exo8 extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo8);

        ((Button) findViewById(R.id.validateButton)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Get text edits
                String _start = ((AutoCompleteTextView) findViewById(R.id.start)).getText().toString();
                String _end  = ((AutoCompleteTextView) findViewById(R.id.end)).getText().toString();

                //Get user's choices
                Bundle bundle=new Bundle();
                bundle.putString(Integer.toString(R.id.start),_start.toUpperCase());
                bundle.putString(Integer.toString(R.id.end),_end.toUpperCase());

                //Create new intent with user's choices
                Intent _intent = new Intent(Exo8.this, Affichage.class);
                _intent.putExtras(bundle);
                startActivity(_intent);
            }
        });
    }
}