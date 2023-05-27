package com.mobile.reverleaf;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentManager;

import java.io.Serializable;

public class EventData implements Serializable {
    public String mID;
    public String mTypeEvent;
    public String mName;
    public String mDescription;
    public String mDate;
    public String mLocation;
    public double mLocationLatitude;
    public double mLocationLongitude;
    public float mPrice;
    public int mNbInscrits;
    public int mNbFavoris;

    public EventData() {
        this.mID = "None";
        this.mTypeEvent = "None";
        this.mName = "None";
        this.mDescription = "None";
        this.mDate = "None";
        this.mLocation = "None";
        this.mLocationLatitude = 0;
        this.mLocationLongitude = 0;
        this.mPrice = 0;
        this.mNbInscrits = 0;
        this.mNbFavoris = 0;
    }

    public EventData(String _name, String _description, String _date, String _location, double _latitude, double _longitude, float _price) {
        this.mID = "None";
        this.mTypeEvent = "None";
        this.mName = _name;
        this.mDescription = _description;
        this.mDate = _date;
        this.mLocation = _location;
        this.mLocationLatitude = _latitude;
        this.mLocationLongitude = _longitude;
        this.mPrice = _price;
        this.mNbInscrits = 0;
        this.mNbFavoris = 0;
    }

    public LinearLayout CreateMyEventCard(Activity _activity, FragmentManager _fragManager)
    {
        return ViewHelper.CreateMyEventCard(_activity.getApplicationContext(), mName, _view -> DisplayEvent(_activity), _view -> RemoveEvent(_fragManager));
    }

    public LinearLayout CreateSearchedCard(Activity _activity)
    {
        return ViewHelper.CreateMyEventCard(_activity.getApplicationContext(), mName, _view -> DisplayEvent(_activity), null);
    }

    public LinearLayout CreateHomeCard(Activity _activity, Boolean _showPrice)
    {
        return ViewHelper.CreateHomeCard(_activity.getApplicationContext(), mName, _showPrice ? mPrice : null, _view -> DisplayEvent(_activity));
    }

    public Bundle DisplayEvent(Activity _activity)
    {
        Bundle _eventDataBundle = new Bundle();
        _eventDataBundle.putString("ID", mID);
        _eventDataBundle.putString(Integer.toString(R.id.eventCategorie), mTypeEvent);
        _eventDataBundle.putString(Integer.toString(R.id.title), mName);
        _eventDataBundle.putString(Integer.toString(R.id.desc), mDescription);
        _eventDataBundle.putString(Integer.toString(R.id.eventDate), mDate);
        _eventDataBundle.putString(Integer.toString(R.id.eventLieu), mLocation);
        _eventDataBundle.putString(Integer.toString(R.id.eventPrice), String.format("%.02f", mPrice));

        _eventDataBundle.putString(Integer.toString(R.id.nbFavoris), Integer.toString(mNbFavoris));
        _eventDataBundle.putString(Integer.toString(R.id.nbInscrits), Integer.toString(mNbInscrits));
        return _eventDataBundle;
    }

    public void RemoveEvent(FragmentManager _fragManager)
    {
        FirebaseManager.DeleteEvent(this);
        ViewHelper.SwitchFragment(_fragManager, MyEvents_Fragment.class);
    }
}
