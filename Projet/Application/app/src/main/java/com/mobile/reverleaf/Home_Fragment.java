package com.mobile.reverleaf;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

public class Home_Fragment extends Fragment {

    LinearLayout mInscriptions, mFavoris, mProximity, mTendances;

    public Home_Fragment() {}


    public static Home_Fragment newInstance(String param1, String param2) {
        Home_Fragment fragment = new Home_Fragment();

        return fragment;
    }

    public void InitializeLayout(View _viewFragment)
    {
        mInscriptions = ViewHelper.GetViewElement(_viewFragment, R.id.inscription_list);
        mFavoris = ViewHelper.GetViewElement(_viewFragment, R.id.favoris_list);
        mProximity = ViewHelper.GetViewElement(_viewFragment, R.id.proximity_list);
        mTendances = ViewHelper.GetViewElement(_viewFragment, R.id.tendance_list);
    }

    public void LoadEvents()
    {
        Activity _activity = getActivity();
        FragmentManager _fragManager = getParentFragmentManager();
        //Events Inscrit
        FirebaseManager.LoadEventsFromUserList(CARD_MOD.EVENTS_INSCRIPTIONS_CARD, _activity, _fragManager, "mIDInscritEvents", _cards->ViewHelper.DisplayCardsLoadedEvents(mInscriptions, _cards));
        //Events Favoris
        FirebaseManager.LoadEventsFromUserList(CARD_MOD.EVENTS_FAVORIS_CARD, _activity, _fragManager, "mIDFavorisEvents", _cards->ViewHelper.DisplayCardsLoadedEvents(mFavoris, _cards));
        //Events Tendances
        FirebaseManager.LoadMostInscriptionEvents(CARD_MOD.EVENTS_TENDANCES_CARD, _activity, _fragManager, 5, _cards->ViewHelper.DisplayCardsLoadedEvents(mTendances, _cards));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_home, container, false);
        InitializeLayout(_view);

        LoadEvents();
        return _view;
    }
}