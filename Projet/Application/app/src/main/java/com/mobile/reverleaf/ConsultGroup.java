package com.mobile.reverleaf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ConsultGroup extends AppCompatActivity {
    
    String mIDGroup;
    LinearLayout mAllMsgLayout;
    TextView mGroupName;
    EditText mMsgText;
    ImageButton mAddUser, mBackButton;
    Button mSendButton;
    Dialog mCurrentPopUp;
    ArrayList<Integer> mEventsIndex = new ArrayList<>();
    ArrayList<String> mEventsID = new ArrayList<>();
    ArrayList<MessageData> mEventsMessagesData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_group);

        InitializeView();
    }

    protected void InitializeView()
    {
        Bundle _bundle = getIntent().getExtras();
        mIDGroup = _bundle.getString("ID");

        InitializeLayouts();
        InitializeTextViews();
        InitializeEditText();
        InitializeButtons();
        FillActivity();
    }

    protected void InitializeLayouts()
    {
        mAllMsgLayout = ViewHelper.GetViewElement(this, R.id.allMessages);
    }

    protected void InitializeTextViews()
    {
        mGroupName = ViewHelper.GetViewElement(this, R.id.groupName);
    }

    protected void InitializeEditText()
    {
        mMsgText = ViewHelper.GetViewElement(this, R.id.messageText);
    }

    protected void InitializeButtons()
    {
        mAddUser = ViewHelper.GetViewElement(this, R.id.addUser);
        ViewHelper.BindOnClick(mAddUser, _view->OpenPopUp());

        mBackButton = ViewHelper.GetViewElement(this, R.id.backButton);
        ViewHelper.BindOnClick(mBackButton, _view->finish());

        mSendButton = ViewHelper.GetViewElement(this, R.id.sendButton);
        ViewHelper.BindOnClick(mSendButton, _view->SendMessage(MESSAGE_TYPE.TEXT_MSG, mMsgText.getText().toString()));
    }

    private void OpenPopUp()
    {
        mCurrentPopUp = ViewHelper.OpenAddUserPopUp(this, R.layout.activity_pop_up_add_user_to_group, mIDGroup, _mail->NotifyUserAdded(_mail), _mail->NotifyUserIn(_mail));
    }

    private void NotifyUserAdded(String _mail)
    {
        SendMessage(MESSAGE_TYPE.GROUP_NOTIFICATION_MSG, _mail+" a été ajouté à la conversation !");
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
            _msg.mTypeMessage = MESSAGE_TYPE.valueOf(_bundle.getString("Message"+_index+"TypeMessage"));
            _msg.mIDSender = _bundle.getString("Message"+_index+"Sender");
            _msg.mDate = _bundle.getString("Message"+_index+"Date");
            _msg.mContent = _bundle.getString("Message"+_index+"Content");
            _allMsg.add(_msg);

            LinearLayout _msgCard;
            switch(_msg.mTypeMessage)
            {
                case EVENT_SHARED_MSG:
                    mEventsIndex.add(i);
                    mEventsMessagesData.add(_msg);
                    mEventsID.add(_msg.mContent);
                    break;

                case TEXT_MSG:
                    _msgCard = ViewHelper.CreateMessage(this, _msg);
                    mAllMsgLayout.addView(_msgCard);
                    break;

                case GROUP_NOTIFICATION_MSG:
                    _msgCard = ViewHelper.CreateGroupNotif(this, _msg);
                    mAllMsgLayout.addView(_msgCard);
                    break;

                default:
                    break;
            }
        }

        FirebaseManager.LoadCorrespondingEvents(CARD_MOD.EVENTS_SHARED, this, getSupportFragmentManager(), mEventsID, _list->SetEventsInChat(_list));
    }

    private void SetEventsInChat(List<LinearLayout> _allCardLayouts)
    {
        List<LinearLayout> _allCardLayoutsEncapsulated = ViewHelper.CreateMessagesSharedEvent(this, _allCardLayouts, mEventsMessagesData);
        for(int i=0; i<_allCardLayoutsEncapsulated.size(); ++i)
            mAllMsgLayout.addView(_allCardLayoutsEncapsulated.get(i),mEventsIndex.get(i));
    }

    private void SendMessage(MESSAGE_TYPE _typMsg, String _contentMsg)
    {
        MessageData _message = new MessageData(_typMsg, _contentMsg);

        Bundle _bundle = getIntent().getExtras();
        String _nbMessages = _bundle.getString("NbMessage");
        if(_nbMessages == null) _nbMessages = "0";
        _bundle.putString("Message"+_nbMessages+"TypeMessage", _message.mTypeMessage.name());
        _bundle.putString("Message"+_nbMessages+"Sender", _message.mIDSender);
        _bundle.putString("Message"+_nbMessages+"Date", _message.mDate);
        _bundle.putString("Message"+_nbMessages+"Content", _message.mContent);
        if(_nbMessages == null) _bundle.putString("NbMessage","1");
        else _bundle.putString("NbMessage",Integer.toString(Integer.parseInt(_nbMessages)+1));


        FirebaseManager.SendMessage(mIDGroup, _message, _msg->ViewHelper.RefreshView(this, _bundle));
    }
}