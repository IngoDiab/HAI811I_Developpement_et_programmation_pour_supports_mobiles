package com.mobile.reverleaf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Search_Result_Fragment extends Fragment {

    LinearLayout mResultList;

    public Search_Result_Fragment() {
        // Required empty public constructor
    }

    public static Search_Result_Fragment newInstance(String param1, String param2) {
        Search_Result_Fragment fragment = new Search_Result_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void InitializeLayout(View _viewFragment)
    {
        mResultList = ViewHelper.GetViewElement(_viewFragment, R.id.searchResult);
    }

    public void LoadResults()
    {
        Bundle _bundle = getArguments();
        if(_bundle == null) return;

        SearchData _searchData = new SearchData();

        _searchData.mTitle = _bundle.getString(Integer.toString(R.id.title));
        _searchData.mCategories = _bundle.getStringArrayList(Integer.toString(R.id.eventCategorie));
        _searchData.mLieu = _bundle.getString(Integer.toString(R.id.eventLieu));
        _searchData.mDateBegin = _bundle.getString(Integer.toString(R.id.dateBegin));
        _searchData.mDateEnd = _bundle.getString(Integer.toString(R.id.dateBegin));

        String _minPrice = _bundle.getString(Integer.toString(R.id.priceMin));
        _searchData.mPriceMin = _minPrice.equals("") ? -1 : Float.parseFloat(_bundle.getString(Integer.toString(R.id.priceMin)).replace("€",""));
        String _maxPrice = _bundle.getString(Integer.toString(R.id.priceMax));
        _searchData.mPriceMax = _maxPrice.equals("") ? -1 : Float.parseFloat(_bundle.getString(Integer.toString(R.id.priceMax)).replace("€",""));

        FirebaseManager.LoadSearchedEvents(CARD_MOD.EVENTS_SEARCHED, getActivity(), getParentFragmentManager(), _searchData, _cards->ViewHelper.DisplayCardsLoadedEvents(mResultList, _cards));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.fragment_search_result, container, false);
        InitializeLayout(_view);

        LoadResults();
        return _view;
    }
}