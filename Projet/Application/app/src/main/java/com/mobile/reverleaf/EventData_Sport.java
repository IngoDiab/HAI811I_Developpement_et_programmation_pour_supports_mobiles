package com.mobile.reverleaf;

import android.app.Activity;
import android.os.Bundle;

public class EventData_Sport extends EventData{
    public String mSportName;
    public String mChampionnat;
    public String mDuree;

    public EventData_Sport()
    {
        super();
        this.mTypeEvent = "Sport";
        this.mSportName = "None";
        this.mChampionnat = "None";
        this.mDuree = "None";
    }

    public EventData_Sport(String _name, String _description, String _date, String _location, float _price, String _sportName, String _championnat, String _duree) {
        super(_name, _description, _date, _location, _price);
        this.mTypeEvent = "Sport";
        this.mSportName = _sportName;
        this.mChampionnat = _championnat;
        this.mDuree = _duree;
    }

    @Override
    public Bundle DisplayEvent(Activity _activity)
    {
        Bundle _eventDataBundle = super.DisplayEvent(_activity);
        _eventDataBundle.putString(Integer.toString(R.id.eventSport), mSportName);
        _eventDataBundle.putString(Integer.toString(R.id.eventChampionnat), mChampionnat);
        _eventDataBundle.putString(Integer.toString(R.id.eventDuree), mDuree);
        ViewHelper.StartNewIntent(_activity, ConsultEvent_Sport.class, _eventDataBundle);
        return _eventDataBundle;
    }
}
