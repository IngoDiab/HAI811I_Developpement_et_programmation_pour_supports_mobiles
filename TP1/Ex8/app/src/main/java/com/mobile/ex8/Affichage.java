package com.mobile.ex8;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class Affichage extends AppCompatActivity
{
    ArrayList<Trip> trips = new Repository().getTrips();

    protected LinearLayout DisplayLocation(String _location, String _hour)
    {
        LinearLayout _locationLayout = new LinearLayout(this);
        _locationLayout.setOrientation(LinearLayout.VERTICAL);
        _locationLayout.setPadding(50,0,50,0);

        TextView _locationText = new TextView(this);
        _locationText.setText(_location);
        _locationLayout.addView(_locationText);

        TextView _hourText = new TextView(this);
        _hourText.setText(_hour);
        _locationLayout.addView(_hourText);

        return _locationLayout;
    }

    protected LinearLayout DisplayTrip(Trip _trip)
    {
        LinearLayout _tripLayout = new LinearLayout(this);
        _tripLayout.setOrientation(LinearLayout.HORIZONTAL);
        _tripLayout.setPadding(0,10,0,50);

        LinearLayout _startLayout = DisplayLocation(_trip.getStart(), _trip.gethStart());
        _tripLayout.addView(_startLayout);

        LinearLayout _endLayout = DisplayLocation(_trip.getFinish(), _trip.gethFinish());
        _tripLayout.addView(_endLayout);

        TextView _costText = new TextView(this);
        NumberFormat _localeCurrency = NumberFormat.getCurrencyInstance(Locale.getDefault());
        _costText.setText(_trip.getPrix()+ " "+_localeCurrency.getCurrency().getSymbol());
        _tripLayout.addView(_costText);

        return _tripLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affich);

        //Receive user's choices
        Bundle data = getIntent().getExtras();
        String _start = data.getString(Integer.toString(R.id.start));
        String _end = data.getString(Integer.toString(R.id.end));

        //Display all trips matching user's choices
        LinearLayout _mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        for (Trip _trip : trips)
        {
            boolean _sameStart = _trip.getStart().toUpperCase().equals(_start);
            if (!_sameStart) continue;
            boolean _sameEnd = _trip.getFinish().toUpperCase().equals(_end);
            if (!_sameEnd) continue;
            LinearLayout _tripLayout = DisplayTrip(_trip);
            _mainLayout.addView(_tripLayout);
        }
    }
}


