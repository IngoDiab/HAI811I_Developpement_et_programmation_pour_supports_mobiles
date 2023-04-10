package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Home extends AppCompatActivity {

    public void SwitchFragment(Fragment _target)
    {
        FragmentManager _fragManager = getSupportFragmentManager();
        _fragManager.beginTransaction()
                .addToBackStack("name")
                .replace(R.id.homePage, _target, null)
                .commit();
    }

    public void BindHomeButton(int _idButton, Fragment _fragmentTarget)
    {
        ImageButton _button = (ImageButton) findViewById(_idButton);
        _button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchFragment(_fragmentTarget);
            }
        });
    }

    public void BindUserBarButtons()
    {
        BindHomeButton(R.id.homeButton, new Home_Fragment());
        BindHomeButton(R.id.searchButton, new Search_Fragment());
        BindHomeButton(R.id.myEventsButton, new MyEvents_Fragment());
        BindHomeButton(R.id.groupsButton, new Group_Fragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BindUserBarButtons();
    }
}