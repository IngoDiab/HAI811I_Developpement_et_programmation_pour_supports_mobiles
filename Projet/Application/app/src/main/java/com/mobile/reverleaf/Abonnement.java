package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.paypal.checkout.order.CaptureOrderResult;

import java.util.ArrayList;
import java.util.List;

public class Abonnement extends AppCompatActivity {

    Button mBackButton;
    LinearLayout mSubsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonnement);

        InitializeView();
        GetAllSubsAvailable();
    }

    public void InitializeView()
    {
        InitializeButtons();
        InitializeLayouts();
    }

    public void InitializeButtons()
    {
        mBackButton = ViewHelper.GetViewElement(this, R.id.backButton);
        ViewHelper.BindOnClick(mBackButton, (_view)->BackToAccueil());
    }

    public void InitializeLayouts()
    {
        mSubsList = ViewHelper.GetViewElement(this, R.id.sub_list);
    }

    public void BackToAccueil()
    {
        FirebaseManager.SignOut();
        ViewHelper.StartNewIntent(this, Accueil.class);
    }

    public void GetAllSubsAvailable()
    {
        FirebaseManager.GetSubscriptionsData(_listData->Display(_listData));
    }

    public void Display(List<AbonnementData> _listData)
    {
        for(AbonnementData _subData : _listData)
        {
            LinearLayout _cardSub = ViewHelper.CreateSubCard(this, _subData.mName, _subData.mPrice, (_view)->OpenPopUpSubscription(_subData));
            mSubsList.addView(_cardSub);
        }
    }

    public void OpenPopUpSubscription(AbonnementData _sub)
    {
        Dialog _popup = ViewHelper.OpenSubscriptionPopUp(this, R.layout.activity_pop_up_abonnements, _sub.mPrice, _captureOrderResult -> ApplySubscription(_sub));

        ViewHelper.GetTextView(_popup, R.id.sub_name).setText(_sub.mName);
        ViewHelper.GetTextView(_popup, R.id.sub_description).setText(_sub.mDescription.replace("\\n", "\n"));
        ViewHelper.GetTextView(_popup, R.id.sub_price).setText(String.format("%.02f", _sub.mPrice));
    }

    public void ApplySubscription(AbonnementData _sub)
    {
        FirebaseManager.ChangeUserDataValue("mSubscription", _sub.mName);
        ViewHelper.StartNewIntent(this, Home.class);
    }
}