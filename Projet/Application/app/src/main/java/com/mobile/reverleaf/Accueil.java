package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Accueil extends AppCompatActivity {

    Button mConnexionButton, mInscriptionButton, mGoogleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        InitializeButton();
    }

    public void InitializeButton()
    {
        mConnexionButton = ViewHelper.GetButton(this, R.id.connexionButton);
        ViewHelper.BindOnClick(mConnexionButton, (_view)->ViewHelper.StartNewIntent(this, Connexion.class));

        mInscriptionButton = ViewHelper.GetButton(this, R.id.inscriptionButton);
        ViewHelper.BindOnClick(mInscriptionButton, (_view)->ViewHelper.StartNewIntent(this, Inscription.class));
    }
}