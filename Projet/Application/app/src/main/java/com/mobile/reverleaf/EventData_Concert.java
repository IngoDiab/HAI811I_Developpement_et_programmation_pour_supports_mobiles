package com.mobile.reverleaf;

import android.app.Activity;
import android.os.Bundle;

public class EventData_Concert extends EventData{
    public String mChanteur;
    public String mTypeMusic;

    public EventData_Concert()
    {
        super();
        this.mTypeEvent = "Concert";
        this.mChanteur = "None";
        this.mTypeMusic = "None";
    }

    public EventData_Concert(String _name, String _description, String _date, String _location, double _latitude, double _longitude, float _price, String _chanteur, String _typeMusic) {
        super(_name, _description, _date, _location, _latitude, _longitude, _price);
        this.mTypeEvent = "Concert";
        this.mChanteur = _chanteur;
        this.mTypeMusic = _typeMusic;
    }

    @Override
    public Bundle DisplayEvent(Activity _activity)
    {
        Bundle _eventDataBundle = super.DisplayEvent(_activity);
        _eventDataBundle.putString(Integer.toString(R.id.eventChanteur), mChanteur);
        _eventDataBundle.putString(Integer.toString(R.id.eventTypeMusic), mTypeMusic);
        ViewHelper.StartNewIntent(_activity, ConsultEvent_Concert.class, _eventDataBundle, false);
        return _eventDataBundle;
    }
}
