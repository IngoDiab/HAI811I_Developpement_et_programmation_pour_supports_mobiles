package com.mobile.reverleaf;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class CreateEvent_Sport extends CreateEvent {
    EditText mSportName, mChampionnat, mDuree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void SetContentView()
    {
        setContentView(R.layout.activity_create_event_sport);
    }

    @Override
    protected void InitializeEditText()
    {
        super.InitializeEditText();
        mSportName = ViewHelper.GetViewElement(this, R.id.editSport);
        mChampionnat = ViewHelper.GetViewElement(this, R.id.editChampionnat);
        mDuree = ViewHelper.GetViewElement(this, R.id.editDuree);
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
        String _sportName = mSportName.getText().toString();
        String _championnat = mChampionnat.getText().toString();
        String _duree = mDuree.getText().toString();
        EventData_Sport _eventSport = new EventData_Sport(_title, _desc, _date, mChosenLieu, _price, _sportName, _championnat, _duree);

        FirebaseManager.RegisterEvent(_eventSport);
        super.RegisterCreatedEvent();
    }
}