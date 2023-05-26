package com.mobile.reverleaf;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class MyEvents_Fragment extends Fragment {

    Button mCreateEventButton;
    LinearLayout mMyEventList;

    public MyEvents_Fragment() {
        // Required empty public constructor
    }

    public static MyEvents_Fragment newInstance(String param1, String param2) {
        MyEvents_Fragment fragment = new MyEvents_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void InitializeButtons(View _viewFragment)
    {
        mCreateEventButton = ViewHelper.GetViewElement(_viewFragment, R.id.createEventButton);
        ViewHelper.BindOnClick(mCreateEventButton, _view->ViewHelper.SwitchFragment(getParentFragmentManager(), Categories_Fragment.class));
    }

    public void InitializeLayout(View _viewFragment)
    {
        mMyEventList = ViewHelper.GetViewElement(_viewFragment, R.id.myEvents_list);
    }

    public void LoadMyEvents()
    {
        FirebaseManager.LoadEventsFromUserList(CARD_MOD.MY_EVENTS_CARD, getActivity(), getParentFragmentManager(), "mIDCreatedEvents", _cards->ViewHelper.DisplayCardsLoadedEvents(mMyEventList, _cards));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.fragment_myevents, container, false);
        InitializeButtons(_view);
        InitializeLayout(_view);

        LoadMyEvents();
        return _view;
    }
}