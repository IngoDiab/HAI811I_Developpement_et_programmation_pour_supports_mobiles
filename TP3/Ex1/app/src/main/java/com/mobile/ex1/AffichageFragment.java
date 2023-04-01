package com.mobile.ex1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AffichageFragment extends Fragment {

    public void GetStringFromBundleToTextView(View _view, Bundle _bundle, int _id)
    {
        String _value = _bundle.getString(Integer.toString(_id));
        ((TextView)_view.findViewById(_id)).setText(_value);
    }

    public void GetListStringFromBundleToTextViewInterest(View _view, Bundle _bundle, String _id)
    {
        ArrayList<Interest> _interests = (ArrayList<Interest>)_bundle.getSerializable(_id);
        LinearLayout _interestsLayout = ((LinearLayout)_view.findViewById(R.id.layoutCentreInteret));
        for(Interest _interest : _interests)
        {
            TextView _interestTextView = new TextView(getActivity());
            _interestTextView.setText(_interest.mName);
            _interestTextView.setPadding(10, 0, 0,10);
            _interestsLayout.addView(_interestTextView);
        }
    }

    public void FillViewFromBundle(View _view)
    {
        Bundle _bundle  = getArguments();
        if(_bundle == null) return;
        GetStringFromBundleToTextView(_view, _bundle, R.id.editName);
        GetStringFromBundleToTextView(_view, _bundle, R.id.editSurname);
        GetStringFromBundleToTextView(_view, _bundle, R.id.editBirth);
        GetStringFromBundleToTextView(_view, _bundle, R.id.editMail);
        GetStringFromBundleToTextView(_view, _bundle, R.id.editMobile);
        GetListStringFromBundleToTextViewInterest(_view, _bundle, "interests");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_affichage, container, false);
        FillViewFromBundle(_view);
        return _view;
    }
}