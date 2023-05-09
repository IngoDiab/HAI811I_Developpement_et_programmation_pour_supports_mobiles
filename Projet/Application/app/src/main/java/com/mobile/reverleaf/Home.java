package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Home extends AppCompatActivity {

    FragmentManager mFragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BindUserBarButtons();
    }

    public void BindHomeButton(int _idButton, Class _fragmentTarget)
    {
        ImageButton _button = ViewHelper.GetViewElement(this, _idButton);
        ViewHelper.BindOnClick(_button, view -> ViewHelper.SwitchFragment(mFragManager, _fragmentTarget));
    }

    public void BindUserBarButtons()
    {
        mFragManager = getSupportFragmentManager();
        BindHomeButton(R.id.homeButton, Home_Fragment.class);
        BindHomeButton(R.id.searchButton, Search_Fragment.class);
        BindHomeButton(R.id.myEventsButton, MyEvents_Fragment.class);
        BindHomeButton(R.id.groupsButton, Group_Fragment.class);
    }
}