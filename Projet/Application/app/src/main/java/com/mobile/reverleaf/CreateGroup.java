package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateGroup extends AppCompatActivity {
    Button mCreateGroup;
    EditText mGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        InitializeButtons();
        InitializeEditText();
    }

    public void InitializeButtons()
    {
        mCreateGroup = ViewHelper.GetViewElement(this, R.id.createButton);
        ViewHelper.BindOnClick(mCreateGroup, _view->CreateGroupFirebase());
    }

    public void InitializeEditText()
    {
        mGroupName = ViewHelper.GetViewElement(this, R.id.groupName);
    }

    private void CreateGroupFirebase()
    {
        FirebaseManager.RegisterGroup(mGroupName.getText().toString(), _group->SwitchToConsult(_group));
    }

    private void SwitchToConsult(GroupData _group)
    {
        Bundle _bundle = new Bundle();
        _bundle.putString(Integer.toString(R.id.groupName), _group.mName);
        _bundle.putString("ID", _group.mID);
        ViewHelper.StartNewIntent(this, ConsultGroup.class, _bundle, true);
    }
}