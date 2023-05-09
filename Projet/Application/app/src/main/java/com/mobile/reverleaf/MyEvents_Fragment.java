package com.mobile.reverleaf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MyEvents_Fragment extends Fragment {

    Button mCreateEventButton;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.fragment_myevents, container, false);
        InitializeButtons(_view);

        return _view;
    }
}