package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class Abonnement extends AppCompatActivity {

    Button mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonnement);

        InitializeButtons();
        Test();
    }

    public void InitializeButtons()
    {
        mBackButton = ViewHelper.GetButton(this, R.id.backButton);
        ViewHelper.BindOnClick(mBackButton, (_view)->BackToAccueil());
    }

    public void BackToAccueil()
    {
        FirebaseManager.SignOut();
        Test();
        ViewHelper.StartNewIntent(this, Accueil.class);
    }

    public void Test()
    {
        FirebaseManager.GetCurrentUserData((_userData)->Display(_userData));
    }

    public void Display(UserData _data)
    {
        Toast.makeText(this, _data.mName, Toast.LENGTH_SHORT).show();
    }
}