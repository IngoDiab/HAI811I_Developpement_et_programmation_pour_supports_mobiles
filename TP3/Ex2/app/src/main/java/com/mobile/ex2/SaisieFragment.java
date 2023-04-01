package com.mobile.ex2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaisieFragment extends Fragment {

    public void AddStringFromViewToBundle(Bundle _bundle, int _id)
    {
        String _value = ((EditText)getView().findViewById(_id)).getText().toString();
        _bundle.putString(Integer.toString(_id), _value);
    }

    public void AddListStringFromCheckboxViewToBundle(Bundle _bundle, List<Integer> _ids)
    {
        ArrayList<Interest> _checkBoxValues = new ArrayList<>();
        for(Integer _id : _ids)
        {
            CheckBox _box = ((CheckBox)getView().findViewById(_id));
            if(_box == null || !_box.isChecked()) continue;
            Interest _interest = new Interest();
            _interest.mName = _box.getText().toString();
            _checkBoxValues.add(_interest);
        }

        _bundle.putSerializable("interests", _checkBoxValues);
    }

    public Bundle GetPackViewElementsValues()
    {
        Bundle _bundle = new Bundle();
        AddStringFromViewToBundle(_bundle, R.id.editName);
        AddStringFromViewToBundle(_bundle, R.id.editSurname);
        AddStringFromViewToBundle(_bundle, R.id.editBirth);
        AddStringFromViewToBundle(_bundle, R.id.editMail);
        AddStringFromViewToBundle(_bundle, R.id.editMobile);
        ArrayList<Integer> _interestList = new ArrayList<Integer>(){{add(R.id.checkSport); add(R.id.checkMusique); add(R.id.checkLecture);}};
        AddListStringFromCheckboxViewToBundle(_bundle,_interestList);
        return _bundle;
    }

    public void SwitchToAffichage()
    {
        Bundle _bundle = GetPackViewElementsValues();
        AffichageFragment _affichageFrag = new AffichageFragment();
        _affichageFrag.setArguments(_bundle);

        FragmentManager _fragManager = getParentFragmentManager();
        _fragManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, _affichageFrag, null)
                    .addToBackStack("SaisieToAffichage")
                    .commit();
    }

    public void BindSubmitButton(View _view)
    {
        ((Button)_view.findViewById(R.id.buttonSubmit)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                SwitchToAffichage();
            }
        });
    }

    public void ClearEditText(int _id)
    {
       ((EditText)getView().findViewById(_id)).setText("");
    }

    public void ClearSomeFields()
    {
        ClearEditText(R.id.editBirth);
        ClearEditText(R.id.editMail);
        ClearEditText(R.id.editMobile);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.fragment_saisie, container, false);
        BindSubmitButton(_view);
        return _view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ClearSomeFields();
    }
}