package com.mobile.reverleaf;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConsultEvent_Concert extends ConsultEvent {
    protected TextView mChanteur, mTypeMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void SetContentView()
    {
        setContentView(R.layout.activity_consult_event_concert);
    }

    @Override
    public void InitializeTextViews()
    {
        super.InitializeTextViews();
        mChanteur = ViewHelper.GetViewElement(this, R.id.eventChanteur);
        mTypeMusic = ViewHelper.GetViewElement(this, R.id.eventTypeMusic);
    }

    @Override
    protected void FillView(Bundle _eventDataBundle)
    {
        super.FillView(_eventDataBundle);
        mChanteur.setText(_eventDataBundle.getString(Integer.toString(R.id.eventChanteur)));
        mTypeMusic.setText(_eventDataBundle.getString(Integer.toString(R.id.eventTypeMusic)));
    }
}