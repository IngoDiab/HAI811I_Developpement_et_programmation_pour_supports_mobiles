package com.mobile.reverleaf;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public abstract class CreateEvent extends AppCompatActivity {

    protected EditText mTitle, mDesc, mPrice;
    protected AutocompleteSupportFragment mLieu;
    protected String mChosenLieu = "";
    LinearLayout mErrorLayout;
    TextView mDate, mErrorText;
    protected Button mCreate, mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetContentView();

        InitializeView();
    }

    protected abstract void SetContentView();

    protected void InitializeView()
    {
        InitializeLayout();
        InitializeTextViews();
        InitializeEditText();
        InitializeButton();
        InitializeFragmentAutoCompletion();

        ViewHelper.BindOnClick(mCreate, _view->RegisterCreatedEvent());
        ViewHelper.BindOnClick(mDate, _view->ViewHelper.OpenCalendar(this, _date -> mDate.setText(_date)));
        ViewHelper.BindOnClick(mBackButton, _view->ViewHelper.StartNewIntent(this, Home.class));
    }

    public void InitializeLayout()
    {
        mErrorLayout = ViewHelper.GetViewElement(this, R.id.errorLayout);
        mErrorLayout.setVisibility(View.GONE);
    }

    public void InitializeTextViews()
    {
        mDate = ViewHelper.GetViewElement(this, R.id.editDate);
        mErrorText = ViewHelper.GetViewElement(this, R.id.textError);
    }

    protected void InitializeButton()
    {
        mBackButton = ViewHelper.GetViewElement(this, R.id.backButton);
        mCreate = ViewHelper.GetViewElement(this, R.id.createButton);
    }

    protected void InitializeEditText()
    {
        mTitle = ViewHelper.GetViewElement(this, R.id.editTitle);
        mDesc = ViewHelper.GetViewElement(this, R.id.editDescription);
        mPrice = ViewHelper.GetViewElement(this, R.id.editPrice);
        mLieu = ViewHelper.GetFragmentElement(getSupportFragmentManager(), R.id.editLieu);
    }

    protected boolean CheckNotEmpty()
    {
        String _title = mTitle.getText().toString();
        String _desc = mDesc.getText().toString();
        String _date = mDate.getText().toString();
        String _price = mPrice.getText().toString();
        if(!_title.equals("") && !_desc.equals("") && !_date.equals("") && !_price.equals("")) return true;
        ViewHelper.WriteInTextView(mErrorText, "Les champs avec une \"*\" sont obligatoires");
        return false;
    }

    protected boolean CheckValidateConditions()
    {
        boolean _noError = CheckNotEmpty();
        if(!_noError) mErrorLayout.setVisibility(View.VISIBLE);
        return _noError;
    }

    protected void InitializeFragmentAutoCompletion()
    {
        mLieu.setPlaceFields((Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS)));

        mLieu.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mChosenLieu = place.getAddress();
            }
        });
    }

    protected void RegisterCreatedEvent()
    {
        ViewHelper.StartNewIntent(this, Home.class);
        ViewHelper.PrintToast(this, "Évènement créé !");
    }
}
