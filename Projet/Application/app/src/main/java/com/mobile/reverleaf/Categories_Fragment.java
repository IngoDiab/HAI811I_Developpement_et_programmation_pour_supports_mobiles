package com.mobile.reverleaf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

public class Categories_Fragment extends Fragment {

    LinearLayout mCategoriesList;

    public Categories_Fragment() {
        // Required empty public constructor
    }
    public static Categories_Fragment newInstance(String param1, String param2) {
        Categories_Fragment fragment = new Categories_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void InitializeLayouts(View _viewFragment)
    {
        mCategoriesList = ViewHelper.GetViewElement(_viewFragment, R.id.categories_list);
    }

    public void GetAllCategoriesAvailable()
    {
        FirebaseManager.GetCategoriesAvailable(_listCat->Display(_listCat));
    }

    public void Display(List<CategoryData> _listCategories)
    {
        for(CategoryData _category : _listCategories)
        {
            try
            {
                Class<?> _classActivity = Class.forName("com.mobile.reverleaf.CreateEvent_"+_category.mName);
                LinearLayout _cardCategory = ViewHelper.CreateCategoryCard(getContext(), _category.mName, _view->ViewHelper.StartNewIntent(getActivity(), _classActivity, false));
                mCategoriesList.addView(_cardCategory);
            } catch (ClassNotFoundException e)
            {
                continue;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_categories, container, false);
        InitializeLayouts(_view);
        GetAllCategoriesAvailable();
        return _view;
    }
}