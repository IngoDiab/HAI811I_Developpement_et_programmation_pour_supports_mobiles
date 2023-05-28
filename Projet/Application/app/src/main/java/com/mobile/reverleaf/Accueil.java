package com.mobile.reverleaf;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;

import java.util.function.Consumer;

public class Accueil extends AppCompatActivity {

    Button mConnexionButton, mInscriptionButton;
    SignInButton mGoogleButton;
    GoogleSignInClient mSignInClient;
    ActivityResultLauncher<Intent> mActivityForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ReverleafManager.InitializeGooglePlaces(getApplication());
        PaypalManager.InitializePaypal(getApplication());

        setContentView(R.layout.activity_accueil);

        InitializeActivityForResult();
        InitializeGoogleSignIn();
        InitializeButton();
    }

    protected void InitializeGoogleSignIn()
    {
        GoogleSignInOptions _signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mSignInClient = GoogleSignIn.getClient(this, _signInOptions);
    }

    protected void InitializeActivityForResult()
    {
        mActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result)
                    {
                        if (result.getResultCode() != Activity.RESULT_OK) return;
                        Intent _data = result.getData();
                        Task<GoogleSignInAccount> _task = GoogleSignIn.getSignedInAccountFromIntent(_data);
                        try
                        {
                            GoogleSignInAccount _account = _task.getResult(ApiException.class);
                            FirebaseManager.GoogleSignIn(_account, Accueil.this, _taskAuth->OnSuccessConnexion());
                        } catch (ApiException e) { throw new RuntimeException(e); }

                    }
                });
    }

    public void OnSuccessConnexion()
    {
        Consumer<UserData> _onUserLoaded = _user -> LoadAbonnementOrHub(_user);
        FirebaseManager.LoadCurrentUserData(_onUserLoaded);
    }

    public void LoadAbonnementOrHub(UserData _user)
    {
        if(_user.mSubscription.equals("None"))
            ViewHelper.StartNewIntent(this, Abonnement.class, false);
        else
            ViewHelper.StartNewIntent(this, Home.class, true);
    }

    protected void InitializeButton()
    {
        mConnexionButton = ViewHelper.GetViewElement(this, R.id.connexionButton);
        ViewHelper.BindOnClick(mConnexionButton, _view->ViewHelper.StartNewIntent(this, Connexion.class, false));

        mInscriptionButton = ViewHelper.GetViewElement(this, R.id.inscriptionButton);
        ViewHelper.BindOnClick(mInscriptionButton, _view->ViewHelper.StartNewIntent(this, Inscription.class, false));

        mGoogleButton = ViewHelper.GetViewElement(this, R.id.inscriptionGoogleButton);
        ViewHelper.BindOnClick(mGoogleButton, _view->SignInGoogle());
    }

    protected void SignInGoogle()
    {
        Intent intent = mSignInClient.getSignInIntent();
        mActivityForResult.launch(intent);
    }

    protected void AuthSignInGoogle(GoogleSignInAccount _account)
    {
        FirebaseManager.GoogleSignIn(_account, this, _taskAuth->OnSuccessConnexion());
    }
}