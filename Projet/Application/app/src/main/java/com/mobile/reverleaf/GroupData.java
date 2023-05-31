package com.mobile.reverleaf;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class GroupData {
    public String mID;
    public String mName;
    public String mIDOwner;
    public List<String> mIDUsers;
    public List<MessageData> mMessages;

    public GroupData()
    {
        mID = "None";
        mIDOwner = "None";
        mName = "None";
        mIDUsers = new ArrayList<>();
        mMessages = new ArrayList<>();
    }

    public LinearLayout CreateHomeCard(Activity _activity)
    {
        return ViewHelper.CreateGroupCard(_activity.getApplicationContext(), _activity.getResources(), mName, _view -> ConsultGroup(_activity));
    }

    public void ConsultGroup(Activity _activity)
    {
        Bundle _bundle = new Bundle();
        _bundle.putString(Integer.toString(R.id.groupName), mName);
        _bundle.putString("ID", mID);
        _bundle.putString("NbMessage", Integer.toString(mMessages.size()));
        for(int i = 0; i < mMessages.size(); ++i)
        {
            String _index = Integer.toString(i);
            MessageData _msg = mMessages.get(i);
            _bundle.putString("Message"+_index+"TypeMessage", _msg.mTypeMessage.name());
            _bundle.putString("Message"+_index+"Sender", _msg.mIDSender);
            _bundle.putString("Message"+_index+"Date", _msg.mDate);
            _bundle.putString("Message"+_index+"Content", _msg.mContent);
        }
        ViewHelper.StartNewIntent(_activity, ConsultGroup.class, _bundle, false);
    }
}
