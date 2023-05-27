package com.mobile.reverleaf;

import android.os.Bundle;
import android.widget.TextView;

public class ConsultEvent_Sport extends ConsultEvent {
    protected TextView mSport, mChampionnat, mDuree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void SetContentView()
    {
        setContentView(R.layout.activity_consult_event_sport);
    }

    @Override
    public void InitializeTextViews()
    {
        super.InitializeTextViews();
        mSport = ViewHelper.GetViewElement(this, R.id.eventSport);
        mChampionnat = ViewHelper.GetViewElement(this, R.id.eventChampionnat);
        mDuree = ViewHelper.GetViewElement(this, R.id.eventDuree);
    }

    @Override
    protected void FillView(Bundle _eventDataBundle)
    {
        super.FillView(_eventDataBundle);
        mSport.setText(_eventDataBundle.getString(Integer.toString(R.id.eventSport)));
        mChampionnat.setText(_eventDataBundle.getString(Integer.toString(R.id.eventChampionnat)));
        mDuree.setText(_eventDataBundle.getString(Integer.toString(R.id.eventDuree))+" minutes");
    }
}