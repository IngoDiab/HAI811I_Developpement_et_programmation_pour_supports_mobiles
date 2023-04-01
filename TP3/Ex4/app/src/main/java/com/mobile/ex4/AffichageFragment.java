package com.mobile.ex4;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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

    public void FillJsonWithTextView(JSONObject _json, int _id)
    {
        TextView _text = ((TextView)getView().findViewById(_id));
        if(_text == null) return;
        try {
            _json.put(Integer.toString(_id), _text.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void CreateJSONFile()
    {
        JSONObject _json = new JSONObject();
        FillJsonWithTextView(_json, R.id.editName);
        FillJsonWithTextView(_json, R.id.editSurname);
        FillJsonWithTextView(_json, R.id.editBirth);
        FillJsonWithTextView(_json, R.id.editMail);
        FillJsonWithTextView(_json, R.id.editMobile);

        String _filename = "UserData";

        FileManager.SaveJson(getContext(), _json, _filename);

        //Check if JSon is successfully saved
        JSONObject _jsonRead = FileManager.LoadJson(getContext(), getActivity().getApplicationContext().getFilesDir(), _filename);
        try {
           String _nameSaved = _jsonRead.getString(Integer.toString(R.id.editName));
            Toast.makeText(getContext(), _nameSaved, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void BindSubmitButton(View _view)
    {
        ((Button)_view.findViewById(R.id.buttonSubmit)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                CreateJSONFile();
            }
        });
    }

    public void BindBackButton(View _view)
    {
        ((Button)_view.findViewById(R.id.buttonBack)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
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
        BindSubmitButton(_view);
        BindBackButton(_view);
        return _view;
    }
}