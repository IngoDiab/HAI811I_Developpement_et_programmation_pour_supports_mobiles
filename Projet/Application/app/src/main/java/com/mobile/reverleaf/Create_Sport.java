package com.mobile.reverleaf;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceTypes;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Create_Sport extends AppCompatActivity {

    EditText mTitle, mDesc, mDate, mPrice;
    AutocompleteSupportFragment mLieu;
    Button mCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sport);

        InitializeView();
    }

    public void InitializeView()
    {
        InitializeEditText();
        InitializeButton();
        InitializeFragmentAutoCompletion();
    }

    private void InitializeButton()
    {
        mCreate = ViewHelper.GetViewElement(this, R.id.createButton);
    }

    private void InitializeEditText()
    {
        mTitle = ViewHelper.GetViewElement(this, R.id.editTitle);
        mDesc = ViewHelper.GetViewElement(this, R.id.editDescription);
        mDate = ViewHelper.GetViewElement(this, R.id.editDate);
        mPrice = ViewHelper.GetViewElement(this, R.id.editPrice);
        mLieu = ViewHelper.GetFragmentElement(getSupportFragmentManager(), R.id.editLieu);
    }

    private void InitializeFragmentAutoCompletion()
    {
        mLieu.setPlaceFields((Arrays.asList(Place.Field.ID, Place.Field.NAME)));

        mLieu.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occured: " + status);
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "Place: " + place.getName()+", "+ place.getId());
            }
        });
    }
}