package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

public class Accueil extends AppCompatActivity {

    Button mConnexionButton, mInscriptionButton, mGoogleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReverleafManager.InitializeGooglePlaces(getApplication());
        PaypalManager.InitializePaypal(getApplication());
        setContentView(R.layout.activity_accueil);
        InitializeButton();
    }

    public void InitializeButton()
    {
        mConnexionButton = ViewHelper.GetViewElement(this, R.id.connexionButton);
        ViewHelper.BindOnClick(mConnexionButton, (_view)->ViewHelper.StartNewIntent(this, Connexion.class));

        mInscriptionButton = ViewHelper.GetViewElement(this, R.id.inscriptionButton);
        ViewHelper.BindOnClick(mInscriptionButton, (_view)->ViewHelper.StartNewIntent(this, Inscription.class));
    }
}