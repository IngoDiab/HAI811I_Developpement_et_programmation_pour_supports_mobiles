package com.mobile.reverleaf;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search_Fragment extends Fragment {

    protected EditText mTitle, mPriceMin, mPriceMax;
    protected TextView mDateBegin, mDateEnd;
    protected List<String> mCategories = new ArrayList<>();
    protected AutocompleteSupportFragment mEditLieuSearched;
    protected String mLieuSearched = "";
    protected Button mSearch;
    protected LinearLayout mCategoriesLayout;

    public Search_Fragment() {
        // Required empty public constructor
    }

    public static Search_Fragment newInstance(String param1, String param2) {
        Search_Fragment fragment = new Search_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void InitializeView(View _view)
    {
        mTitle = ViewHelper.GetViewElement(_view, R.id.editTitle);
        mDateBegin = ViewHelper.GetViewElement(_view, R.id.dateBegin);
        mDateEnd = ViewHelper.GetViewElement(_view, R.id.dateEnd);
        mPriceMin = ViewHelper.GetViewElement(_view, R.id.priceMin);
        mPriceMax = ViewHelper.GetViewElement(_view, R.id.priceMax);
        mEditLieuSearched = ViewHelper.GetFragmentElement(getChildFragmentManager(), R.id.editLieu);
        mSearch = ViewHelper.GetViewElement(_view, R.id.buttonSearch);
        mCategoriesLayout = ViewHelper.GetViewElement(_view, R.id.categories_list);

        ViewHelper.InitializeFragmentAutoCompletion(mEditLieuSearched, Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS), place -> {mLieuSearched = place.getAddress();});
        ViewHelper.BindOnClick(mDateBegin, _v->ViewHelper.OpenCalendar(getContext(), _date -> mDateBegin.setText(_date)));
        ViewHelper.BindOnClick(mDateEnd, _v->ViewHelper.OpenCalendar(getContext(), _date -> mDateEnd.setText(_date)));
    }

    protected void BindButtons()
    {
        ViewHelper.BindOnClick(mSearch, _view->Search());
    }

    protected void Search()
    {
        Bundle _bundleSearch = new Bundle();

        _bundleSearch.putString(Integer.toString(R.id.title), mTitle.getText().toString());
        ArrayList<String> _categories = new ArrayList<>();
        for(String _category : mCategories)
            _categories.add(_category);
        _bundleSearch.putStringArrayList(Integer.toString(R.id.eventCategorie), _categories);
        _bundleSearch.putString(Integer.toString(R.id.eventLieu), mLieuSearched);
        _bundleSearch.putString(Integer.toString(R.id.dateBegin), mDateBegin.getText().toString());
        _bundleSearch.putString(Integer.toString(R.id.dateEnd), mDateEnd.getText().toString());
        _bundleSearch.putString(Integer.toString(R.id.priceMin), mPriceMin.getText().toString());
        _bundleSearch.putString(Integer.toString(R.id.priceMax), mPriceMax.getText().toString());

        ViewHelper.SwitchFragment(getParentFragmentManager(), new Search_Result_Fragment(), _bundleSearch);
    }

    protected void LoadCategories()
    {
        FirebaseManager.GetCategoriesAvailable(_listCat->Display(_listCat));
    }

    public void Display(List<CategoryData> _listCategories)
    {
        for(CategoryData _category : _listCategories)
        {
            LinearLayout _cardCategory = ViewHelper.CreateHomeCard(getActivity(), _category.mName, null, _view -> AddToCategoriesSearched(_view, _category));
            mCategoriesLayout.addView(_cardCategory);
        }
    }

    protected void AddToCategoriesSearched(View _card, CategoryData _category)
    {
        String _name = _category.mName;
        if(mCategories.contains(_name))
        {
            mCategories.remove(_name);
            _card.setAlpha(1);
        }
        else
        {
            mCategories.add(_name);
            _card.setAlpha(0.5f);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_search, container, false);

        InitializeView(_view);
        BindButtons();
        LoadCategories();

        return _view;
    }
}