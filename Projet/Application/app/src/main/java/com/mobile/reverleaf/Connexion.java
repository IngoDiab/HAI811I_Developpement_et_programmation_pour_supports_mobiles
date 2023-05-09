package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.function.Consumer;

public class Connexion extends AppCompatActivity {

    EditText mEmailEdit, mPasswordEdit;
    Button mConnexionButton, mBackButton;
    CheckBox mCheckboxRemainConnected;
    LinearLayout mErrorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        InitializeLayout();
        InitializeButtons();
        InitializeEditTexts();
        InitializeCheckbox();
    }

    public void InitializeLayout()
    {
        mErrorLayout = ViewHelper.GetViewElement(this, R.id.connexionErrorLayout);
        mErrorLayout.setVisibility(View.GONE);
    }

    public void InitializeButtons()
    {
        mConnexionButton = ViewHelper.GetViewElement(this, R.id.connexionButton);
        ViewHelper.BindOnClick(mConnexionButton, (_view)->ConnectUser(_view));

        mBackButton = ViewHelper.GetViewElement(this, R.id.backButton);
        ViewHelper.BindOnClick(mBackButton, (_view)->ViewHelper.StartNewIntent(this, Accueil.class));
    }

    public void InitializeEditTexts()
    {
        mEmailEdit = ViewHelper.GetViewElement(this, R.id.editEmailAddress);
        mPasswordEdit = ViewHelper.GetViewElement(this, R.id.editPassword);
    }

    public void InitializeCheckbox()
    {
        mCheckboxRemainConnected = ViewHelper.GetViewElement(this, R.id.checkBoxRemainConnected);
    }

    public void ConnectUser(View _view)
    {
        mErrorLayout.setVisibility(View.GONE);

        String _mail = mEmailEdit.getText().toString();
        String _password = mPasswordEdit.getText().toString();

        if(_mail.equals("") || _password.equals(""))
        {
            OnFailedConnexion();
            return;
        }

        Consumer<Task<AuthResult>> _onSuccessCallback = (_task)->OnSuccessConnexion();
        Consumer<Task<AuthResult>> _onFailCallback = (_task)->OnFailedConnexion();

        FirebaseManager.ConnectUser(this, _mail, _password, _onSuccessCallback, _onFailCallback);
    }

    public void OnSuccessConnexion()
    {
        Consumer<UserData> _onUserLoaded = _user -> LoadAbonnementOrHub(_user);
        FirebaseManager.LoadCurrentUserData(_onUserLoaded,null);
    }

    public void LoadAbonnementOrHub(UserData _user)
    {
        if(_user.mSubscription.equals("None"))
            ViewHelper.StartNewIntent(this, Abonnement.class);
        else
            ViewHelper.StartNewIntent(this, Home.class);
    }

    public void OnFailedConnexion()
    {
        mErrorLayout.setVisibility(View.VISIBLE);
        mPasswordEdit.setText("");
    }
}