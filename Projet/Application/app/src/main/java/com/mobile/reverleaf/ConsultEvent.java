package com.mobile.reverleaf;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public abstract class ConsultEvent extends AppCompatActivity {
    protected TextView mTitle, mDesc, mNbFavoris, mNbInscrits;
    protected Button mCategorie, mDate, mLieu, mPrice, mBackButton;
    protected List<Button> mSelectedButtons = new ArrayList<>();
    protected ImageButton mFavoris;
    protected Button mInscription, mSearchSame;
    protected LinearLayout mLayout;

    protected boolean mUserIsOwnerEvent = false, mUserIsRegisteredToThisEvent = false, mUserHasInFavoris = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetContentView();

        InitializeView();
        FillView(getIntent().getExtras());
    }

    protected abstract void SetContentView();

    protected void InitializeView()
    {
        InitializeLayouts();
        InitializeTextViews();
        InitializeButton();
        BindButtons();
    }

    protected void InitializeLayouts()
    {
        mLayout = ViewHelper.GetViewElement(this, R.id.layoutEvent);
    }

    protected void InitializeTextViews()
    {
        mTitle = ViewHelper.GetViewElement(this, R.id.title);
        mDesc = ViewHelper.GetViewElement(this, R.id.desc);
        mNbFavoris = ViewHelper.GetViewElement(this, R.id.nbFavoris);
        mNbInscrits = ViewHelper.GetViewElement(this, R.id.nbInscrits);
    }

    protected void InitializeButton()
    {
        mFavoris = ViewHelper.GetViewElement(this, R.id.favorisButton);

        mCategorie = ViewHelper.GetViewElement(this, R.id.eventCategorie);
        mDate = ViewHelper.GetViewElement(this, R.id.eventDate);
        mLieu = ViewHelper.GetViewElement(this, R.id.eventLieu);
        mPrice = ViewHelper.GetViewElement(this, R.id.eventPrice);

        mInscription = ViewHelper.GetViewElement(this, R.id.eventInscription);
        mSearchSame = ViewHelper.GetViewElement(this, R.id.eventSearchSame);

        mBackButton = ViewHelper.GetViewElement(this, R.id.backButton);
    }

    protected void BindButtons()
    {
        ViewHelper.BindOnClick(mFavoris, _view->ManageFavorisEvent(!mUserHasInFavoris));

        ViewHelper.BindOnClick(mCategorie, _view->ToggleSearch(mCategorie));
        ViewHelper.BindOnClick(mDate, _view->ToggleSearch(mDate));
        ViewHelper.BindOnClick(mLieu, _view->ToggleSearch(mLieu));
        ViewHelper.BindOnClick(mPrice, _view->ToggleSearch(mPrice));

        ViewHelper.BindOnClick(mInscription, _view->ManageInscriptionEvent(!mUserIsRegisteredToThisEvent));
        ViewHelper.BindOnClick(mSearchSame, _view->SearchSame());

        ViewHelper.BindOnClick(mBackButton, _view->finish());
    }

    protected void SearchSame()
    {
        Bundle _bundleSearch = new Bundle();

        ArrayList<String> _categories = new ArrayList<>();
        if(!FillSearchWithButton(mCategorie).equals("")) _categories.add(FillSearchWithButton(mCategorie));
        _bundleSearch.putStringArrayList(Integer.toString(R.id.eventCategorie), _categories);
        _bundleSearch.putString(Integer.toString(R.id.eventLieu), FillSearchWithButton(mLieu));
        _bundleSearch.putString(Integer.toString(R.id.dateBegin), FillSearchWithButton(mDate));
        _bundleSearch.putString(Integer.toString(R.id.dateEnd), FillSearchWithButton(mDate));
        _bundleSearch.putString(Integer.toString(R.id.priceMin), FillSearchWithButton(mPrice));
        _bundleSearch.putString(Integer.toString(R.id.priceMax), FillSearchWithButton(mPrice));
        _bundleSearch.putString("methodName", "DisplayResultSearch");

        ViewHelper.StartNewIntent(this, Home.class, _bundleSearch, true);
    }

    protected String FillSearchWithButton(Button _field)
    {
        if(!mSelectedButtons.contains(_field)) return "";
        return _field.getText().toString();
    }

    protected void ManageInscriptionEvent(boolean _addOrRemoveInscrit)
    {
        Bundle _bundle = getIntent().getExtras();
        String _eventID = _bundle.getString("ID");
        FirebaseManager.ManageInteractionWithEvent(_addOrRemoveInscrit, _eventID, "mIDInscritEvents", "mNbInscrits", _id->RefreshEvent(_id));

        if(mUserHasInFavoris) ManageFavorisEvent(false);
    }

    protected void ManageFavorisEvent(boolean _addOrRemoveFavoris)
    {
        Bundle _bundle = getIntent().getExtras();
        String _eventID = _bundle.getString("ID");
        FirebaseManager.ManageInteractionWithEvent(_addOrRemoveFavoris, _eventID, "mIDFavorisEvents", "mNbFavoris", _id->RefreshEvent(_id));
    }

    protected void ToggleSearch(Button _button)
    {
        if(mSelectedButtons.contains(_button))
        {
            mSelectedButtons.remove(_button);
            _button.setBackgroundColor(ContextCompat.getColor(this, R.color.mainColor));
        }
        else
        {
            mSelectedButtons.add(_button);
            _button.setBackgroundColor(ContextCompat.getColor(this, R.color.clickedColor));
        }
    }

    protected void RefreshEvent(String _eventID)
    {
        mUserIsOwnerEvent = mUserIsRegisteredToThisEvent = mUserHasInFavoris = false;
        FirebaseManager.IsEventInUserList("mIDCreatedEvents", _eventID, _isOwner->RefreshLayout(_isOwner, false, false));
        FirebaseManager.IsEventInUserList("mIDInscritEvents", _eventID, _isRegistered->RefreshLayout(false, _isRegistered, false));
        FirebaseManager.IsEventInUserList("mIDFavorisEvents", _eventID, _isFavoris->RefreshLayout(false, false, _isFavoris));
    }

    protected void RefreshLayout(boolean _isOwner, boolean _isRegistered, boolean _isFavoris)
    {
        mUserIsOwnerEvent |= _isOwner;
        mUserIsRegisteredToThisEvent |= _isRegistered;
        mUserHasInFavoris |= _isFavoris;
        mInscription.setVisibility(mUserIsOwnerEvent ? View.GONE : View.VISIBLE);
        mInscription.setText(mUserIsRegisteredToThisEvent ? "Desinscription" : "Inscription");
        mFavoris.setVisibility(mUserIsOwnerEvent || mUserIsRegisteredToThisEvent ? View.GONE : View.VISIBLE);
        mFavoris.setImageResource(mUserHasInFavoris ? R.drawable.favoris_icon : R.drawable.not_favoris_icon);
    }

    protected void FillView(Bundle _eventDataBundle)
    {
        String _eventID = _eventDataBundle.getString("ID");
        RefreshEvent(_eventID);

        mCategorie.setText(_eventDataBundle.getString(Integer.toString(R.id.eventCategorie)));
        mTitle.setText(_eventDataBundle.getString(Integer.toString(R.id.title)));
        mDesc.setText(_eventDataBundle.getString(Integer.toString(R.id.desc)));
        mDate.setText(_eventDataBundle.getString(Integer.toString(R.id.eventDate)));
        mLieu.setText(_eventDataBundle.getString(Integer.toString(R.id.eventLieu)));
        mPrice.setText(_eventDataBundle.getString(Integer.toString(R.id.eventPrice))+"€");

        mNbFavoris.setText(_eventDataBundle.getString(Integer.toString(R.id.nbFavoris)));
        mNbInscrits.setText(_eventDataBundle.getString(Integer.toString(R.id.nbInscrits)));
    }

}
