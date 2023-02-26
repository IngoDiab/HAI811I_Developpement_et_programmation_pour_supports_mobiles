package com.mobile.ex9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    HashMap<String,List<String>> mAgenda = new HashMap<String,List<String>>(){{
            put("10/02/2023",  new ArrayList<String>(Arrays.asList("Premier évènement à cette date", "Second évènement à cette date")));
            put("24/02/2023",  new ArrayList<String>(Arrays.asList("Vacances")));
    }};

    public void LoadAgenda(TableLayout _table)
    {
        for(Map.Entry<String, List<String>> _pair : mAgenda.entrySet())
        {
            TableRow _row = new TableRow(this);
            _table.addView(_row);

            //DATE
            TextView _date = new TextView(this);
            _date.setText(_pair.getKey());
            _row.addView(_date);

            LinearLayout _eventsLayout = new LinearLayout(this);
            _eventsLayout.setOrientation(LinearLayout.VERTICAL);
            _eventsLayout.setPadding(50,0,0,50);
            _row.addView(_eventsLayout);

            //EVENTS
            for(String _event : _pair.getValue())
            {
                TextView _eventName = new TextView(this);
                _eventName.setText(_event);
                _eventsLayout.addView(_eventName);
            }
        }
    }

    public void AddEventInfo(TableLayout _table)
    {
        LinearLayout _addEventLayout = new LinearLayout(this);
        _addEventLayout.setPadding(0,50,0,0);
        _addEventLayout.setOrientation(LinearLayout.VERTICAL);
        _table.addView(_addEventLayout);

        DatePicker _dateNewEvent = new DatePicker(this);
        _addEventLayout.addView(_dateNewEvent);

        EditText _titleNewEvent = new EditText(this);
        _titleNewEvent.setHint(R.string.addEvent_placeholder);
        _addEventLayout.addView(_titleNewEvent);

        Button _button = new Button(this);
        _button.setText(R.string.validateEvent);
        _addEventLayout.addView(_button);
        _button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String _dateSelected =
                        (_dateNewEvent.getDayOfMonth() < 9 ? "0" : "") + Integer.toString(_dateNewEvent.getDayOfMonth())+"/"
                        + (_dateNewEvent.getMonth()+1 < 9 ? "0" : "") + Integer.toString(_dateNewEvent.getMonth()+1)+"/"
                        +Integer.toString(_dateNewEvent.getYear());

                String _eventName = _titleNewEvent.getText().toString();
                if(!mAgenda.containsKey(_dateSelected)) mAgenda.put(_dateSelected,  new ArrayList<String>(Arrays.asList(_eventName)));
                else mAgenda.get(_dateSelected).add(_eventName);
                _table.removeAllViews();
                LoadAgenda(_table);
                AddEventInfo(_table);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout _table = (TableLayout)findViewById(R.id.MainTable);
        LoadAgenda(_table);
        AddEventInfo(_table);
    }
}