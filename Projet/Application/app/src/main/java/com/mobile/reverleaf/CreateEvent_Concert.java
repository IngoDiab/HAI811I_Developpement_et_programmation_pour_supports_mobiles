package com.mobile.reverleaf;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class CreateEvent_Concert extends CreateEvent {
    EditText mChanteur, mTypeMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void SetContentView()
    {
        setContentView(R.layout.activity_create_event_concert);
    }

    @Override
    protected void InitializeEditText()
    {
        super.InitializeEditText();
        mChanteur = ViewHelper.GetViewElement(this, R.id.eventChanteur);
        mTypeMusic = ViewHelper.GetViewElement(this, R.id.eventTypeMusic);
    }

    @Override
    protected void RegisterCreatedEvent()
    {
        mErrorLayout.setVisibility(View.GONE);
        if(!CheckValidateConditions()) return;
        String _title = mTitle.getText().toString();
        String _desc = mDesc.getText().toString();
        String _date = mDate.getText().toString();
        float _price = Float.parseFloat(mPrice.getText().toString());
        String _chanteur = mChanteur.getText().toString();
        String _typeMusic = mTypeMusic.getText().toString();
        EventData_Concert _eventConcert = new EventData_Concert(_title, _desc, _date, mChosenLieuAdress, mChosenLieuLatLng.latitude, mChosenLieuLatLng.longitude, _price, _chanteur, _typeMusic);

        FirebaseManager.RegisterEvent(_eventConcert);
        super.RegisterCreatedEvent();
    }
}