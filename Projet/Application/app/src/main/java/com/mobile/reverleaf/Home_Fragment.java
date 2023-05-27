package com.mobile.reverleaf;

import android.app.Activity;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

public class Home_Fragment extends Fragment implements LocationListener {

    LocationManager mLocationManager;
    LinearLayout mInscriptions, mFavoris, mProximity, mTendances;
    Button mRefreshProximity;
    double mProximityDistanceMax = 30000; //meters

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

    public void BindButtons(View _viewFragment)
    {
        mRefreshProximity = ViewHelper.GetViewElement(_viewFragment, R.id.refreshButton);
        ViewHelper.BindOnClick(mRefreshProximity, _view->AskPermissions());
    }

    public void LoadEvents()
    {
        mInscriptions.removeAllViews();
        mFavoris.removeAllViews();
        mTendances.removeAllViews();
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
    public void onResume()
    {
        super.onResume();
        AskPermissions();
        LoadEvents();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mLocationManager.removeUpdates(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_home, container, false);
        InitializeLayout(_view);
        BindButtons(_view);
        return _view;
    }

    protected void AskPermissions()
    {
        Activity _activity = getActivity();
        mLocationManager = (LocationManager) _activity.getSystemService(Context.LOCATION_SERVICE);
        boolean _needPermissionLocation = ContextCompat.checkSelfPermission(_activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED;
        if(_needPermissionLocation) requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        else GetGPSLocation();
    }

    public ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        GetGPSLocation();
                    } else {
                        ViewHelper.PrintToast(getActivity(), "Evènements à proximité non disponible");
                    }
                }
            }
    );

    protected void GetGPSLocation()
    {
        Activity _activity = getActivity();
        boolean _needPermissionLocation = ContextCompat.checkSelfPermission(_activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED;

        if (_needPermissionLocation) return;
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1000, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location _userLocation)
    {
        mProximity.removeAllViews();
        Activity _activity = getActivity();
        FragmentManager _fragManager = getParentFragmentManager();
        FirebaseManager.LoadProximityEvents(CARD_MOD.EVENTS_PROXIMITY_CARD, _activity, _fragManager, _userLocation, mProximityDistanceMax, _cards->ViewHelper.DisplayCardsLoadedEvents(mProximity, _cards));
        ViewHelper.PrintToast(getActivity(), "Les évènements à proximité ont été mis à jour");
        mLocationManager.removeUpdates(this);
    }
}