package com.mobile.ex7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback  {

    private LocationManager mLocationManager;
    private LatLng mPosition;
    private SupportMapFragment mSuportMapFragment;
    private GoogleMap mMap;

    protected void InitializeCenterButton()
    {
        ((Button)findViewById(R.id.centerButton)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 15));
            }
        });
    }

    protected void AccessGPSLocation()
    {
        boolean _needPermissionLocation = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED;

        if (_needPermissionLocation)
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },0);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    protected void SetGPSMarkerOnMap(@NonNull Location location)
    {
        if(mMap == null) return;
        mPosition = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions _markerOptions = new MarkerOptions().position(mPosition);
        mMap.addMarker(_markerOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeCenterButton();

        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mSuportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mSuportMapFragment.getMapAsync(this);

        AccessGPSLocation();
    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        SetGPSMarkerOnMap(location);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
}