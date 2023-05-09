package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.function.Consumer;

public class Inscription extends AppCompatActivity {

    EditText mEmailEdit, mPhoneEdit, mPasswordEdit,  mConfirmPasswordEdit, mNameEdit, mSurnameEdit, mAddressEdit;
    Button mInscriptionButton, mBackButton;
    CheckBox mCheckboxConditions;
    LinearLayout mErrorLayout;
    TextView mErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        InitializeLayout();
        InitializeEditTexts();
        InitializeTextViews();
        InitializeButtons();
        InitializeCheckbox();
    }

    public void InitializeLayout()
    {
        mErrorLayout = ViewHelper.GetViewElement(this, R.id.connexionErrorLayout);
        mErrorLayout.setVisibility(View.GONE);
    }

    public void InitializeButtons()
    {
        mInscriptionButton = ViewHelper.GetViewElement(this, R.id.inscriptionButton);
        ViewHelper.BindOnClick(mInscriptionButton, (_view)->RegisterUser(_view));

        mBackButton = ViewHelper.GetViewElement(this, R.id.backButton);
        ViewHelper.BindOnClick(mBackButton, (_view)->ViewHelper.StartNewIntent(this, Accueil.class));
    }

    public void InitializeEditTexts()
    {
        mEmailEdit = ViewHelper.GetViewElement(this, R.id.editEmailAddress);
        mPhoneEdit = ViewHelper.GetViewElement(this, R.id.editTelephone);
        mPasswordEdit = ViewHelper.GetViewElement(this, R.id.editPassword);
        mConfirmPasswordEdit = ViewHelper.GetViewElement(this, R.id.editConfirmPassword);
        mNameEdit = ViewHelper.GetViewElement(this, R.id.editName);
        mSurnameEdit = ViewHelper.GetViewElement(this, R.id.editSurname);
        mAddressEdit = ViewHelper.GetViewElement(this, R.id.editAdress);
    }

    public void InitializeTextViews()
    {
        mErrorText = ViewHelper.GetViewElement(this, R.id.textError);
    }

    public void InitializeCheckbox()
    {
        mCheckboxConditions = ViewHelper.GetViewElement(this, R.id.checkBoxAccept);
    }

    public boolean CheckPasswordConfirmed()
    {
        String _password = mPasswordEdit.getText().toString();
        String _confirmPassword = mConfirmPasswordEdit.getText().toString();
        if(_password.equals(_confirmPassword)) return true;
        ViewHelper.WriteInTextView(mErrorText, "Erreur dans la confirmation du mot de passe");
        return false;
    }

    public boolean CheckboxChecked()
    {
        if(mCheckboxConditions.isChecked()) return true;
        ViewHelper.WriteInTextView(mErrorText, "Vous devez accepter les conditions");
        return false;
    }

    public boolean CheckNotEmpty()
    {
        String _mail = mEmailEdit.getText().toString();
        String _phone = mPhoneEdit.getText().toString();
        String _password = mPasswordEdit.getText().toString();
        String _confirmPassword = mConfirmPasswordEdit.getText().toString();
        if(!_mail.equals("") && !_phone.equals("") && !_password.equals("") && !_confirmPassword.equals("")) return true;
        ViewHelper.WriteInTextView(mErrorText, "Les champs avec une \"*\" sont obligatoires");
        return false;
    }

    public boolean CheckValidateConditions()
    {
        boolean _noError = CheckNotEmpty() && CheckboxChecked() && CheckPasswordConfirmed();
        if(!_noError) mErrorLayout.setVisibility(View.VISIBLE);
        return _noError;
    }

    public void RegisterUser(View _view)
    {
        mErrorLayout.setVisibility(View.GONE);
        if(!CheckValidateConditions()) return;

        String _mail = mEmailEdit.getText().toString();
        String _phone = mPhoneEdit.getText().toString();
        String _password = mPasswordEdit.getText().toString();
        String _name = mNameEdit.getText().toString();
        String _surname = mSurnameEdit.getText().toString();
        String _address = mAddressEdit.getText().toString();

        UserData _user = new UserData(_mail, _phone, _password, _name, _surname, _address, "None");

        Consumer<Task<AuthResult>> _onSuccessCallback = (_task)->OnSuccessInscription();
        Consumer<Task<AuthResult>> _onFailCallback = (_task)->OnFailedInscription();

        FirebaseManager.RegisterUser(this, _user, _onSuccessCallback, _onFailCallback);
    }

    public void OnSuccessInscription()
    {
        FirebaseManager.LoadCurrentUserData(_userData->ViewHelper.StartNewIntent(this, Abonnement.class), null);
    }

    public void OnFailedInscription()
    {
        ViewHelper.WriteInTextView(mErrorText, "L'adresse mail doit être de la forme abc@def.xyz et le mot de passe doit contenir minimum 6 caractères");
        mErrorLayout.setVisibility(View.VISIBLE);
        mConfirmPasswordEdit.setText("");
    }
}