package com.mobile.reverleaf;

import java.util.ArrayList;
import java.util.List;

public class SearchData {
    String mTitle;
    List<String> mCategories;
    String mLieu;
    String mDateBegin;
    String mDateEnd;
    float mPriceMin;
    float mPriceMax;

    public SearchData()
    {
        mTitle = "";
        mCategories = new ArrayList<>();
        mLieu = "";
        mDateBegin = "";
        mDateEnd = "";
        mPriceMin = 0;
        mPriceMax = 0;
    }

    public SearchData(String _title, List<String> _categories, String _lieu, String _dateBegin, String _dateEnd, float _priceMin, float _priceMax)
    {
        mTitle = _title;
        mCategories = _categories;
        mLieu = _lieu;
        mDateBegin = _dateBegin;
        mDateEnd = _dateEnd;
        mPriceMin = _priceMin;
        mPriceMax = _priceMax;
    }
}
