package com.mobile.reverleaf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Group_Fragment extends Fragment {

    Button mCreateGroupButton;
    LinearLayout mGroupList;

    public Group_Fragment() {
        // Required empty public constructor
    }

    public static Group_Fragment newInstance(String param1, String param2) {
        Group_Fragment fragment = new Group_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void InitializeButtons(View _viewFragment)
    {
        mCreateGroupButton = ViewHelper.GetViewElement(_viewFragment, R.id.createGroup);
        ViewHelper.BindOnClick(mCreateGroupButton, _view->ViewHelper.StartNewIntent(getActivity(), CreateGroup.class, false));
    }

    private void InitializeLayout(View _viewFragment)
    {
        mGroupList = ViewHelper.GetViewElement(_viewFragment, R.id.group_list);
    }

    private void LoadGroups()
    {
        mGroupList.removeAllViews();
        FirebaseManager.LoadGroups(_group->{LinearLayout _groupCard = _group.CreateHomeCard(getActivity()); mGroupList.addView(_groupCard);});
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        LoadGroups();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View _view = inflater.inflate(R.layout.fragment_group, container, false);
        InitializeLayout(_view);
        InitializeButtons(_view);
        return _view;
    }
}