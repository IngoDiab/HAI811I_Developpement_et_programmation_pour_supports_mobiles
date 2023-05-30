package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ConsultGroup extends AppCompatActivity {

    TextView mGroupName;
    ImageButton mAddUser, mBackButton;
    Dialog mCurrentPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_group);

        InitializeTextViews();
        InitializeButtons();
        FillActivity();
    }

    protected void InitializeTextViews()
    {
        mGroupName = ViewHelper.GetViewElement(this, R.id.groupName);
    }

    protected void InitializeButtons()
    {
        mAddUser = ViewHelper.GetViewElement(this, R.id.addUser);
        ViewHelper.BindOnClick(mAddUser, _view->OpenPopUp());

        mBackButton = ViewHelper.GetViewElement(this, R.id.backButton);
        ViewHelper.BindOnClick(mBackButton, _view->finish());
    }

    private void OpenPopUp()
    {
        Bundle _bundle = getIntent().getExtras();
        mCurrentPopUp = ViewHelper.OpenAddUserPopUp(this, R.layout.activity_pop_up_add_user_to_group, _bundle.getString("ID"), _mail->NotifyUserAdded(_mail), _mail->NotifyUserIn(_mail));
    }

    private void NotifyUserAdded(String _mail)
    {
        ViewHelper.PrintToast(this, "L'utilisateur avec l'adresse "+ _mail +" a été ajouté.");
        mCurrentPopUp.dismiss();
        mCurrentPopUp = null;
    }

    private void NotifyUserIn(String _mail)
    {
        ViewHelper.PrintToast(this, "L'utilisateur avec l'adresse "+ _mail +" est déjà dans le groupe.");
    }

    private void FillActivity()
    {
        Bundle _bundle = getIntent().getExtras();
        mGroupName.setText(_bundle.getString(Integer.toString(R.id.groupName)));

        List<MessageData> _allMsg = new ArrayList<>();
        String _nbMessages = _bundle.getString("NbMessage");
        if(_nbMessages == null) return;
        int _nbMsg = Integer.parseInt(_nbMessages);
        for(int i = 0; i < _nbMsg; ++i)
        {
            String _index = Integer.toString(i);
            MessageData _msg = new MessageData();
            _msg.mIDSender = _bundle.getString("Message"+_index+"Sender");
            _msg.mDate = _bundle.getString("Message"+_index+"Date");
            _msg.mContent = _bundle.getString("Message"+_index+"Content");
            _allMsg.add(_msg);
        }
    }
}