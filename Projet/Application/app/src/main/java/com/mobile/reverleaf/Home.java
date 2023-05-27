package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.ImageButton;

public class Home extends AppCompatActivity {

    FragmentManager mFragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitializeActivity();
    }

    protected void InitializeActivity()
    {
        Bundle _bundle = getIntent().getExtras();
        if(_bundle == null)
        {
            DefaultBehaviour();
            return;
        };
        String _methodName = _bundle.getString("methodName");

        if(_methodName == null) DefaultBehaviour();

        switch (_methodName)
        {
            case "DisplayResultSearch":
                DisplayResultSearch(_bundle);
                break;

            default:
                DefaultBehaviour();
                break;
        }
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

    public void BindButtons()
    {
        ImageButton _button = ViewHelper.GetViewElement(this, R.id.disconnectButton);
        ViewHelper.BindOnClick(_button, view -> Disconnect());
    }

    protected void Disconnect()
    {
        FirebaseManager.SignOut();
        ViewHelper.StartNewIntent(this, Accueil.class, true);
    }

    protected void DefaultBehaviour()
    {
        BindUserBarButtons();
        BindButtons();
    }

    protected void DisplayResultSearch(Bundle _bundle)
    {
        DefaultBehaviour();
        ViewHelper.SwitchFragment(getSupportFragmentManager(), new Search_Result_Fragment(), _bundle);
    }
}