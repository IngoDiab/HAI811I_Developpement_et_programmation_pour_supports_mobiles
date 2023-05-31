package com.mobile.reverleaf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MessageData {
    public MESSAGE_TYPE mTypeMessage;
    public String mIDSender;
    public String mDate;
    public String mContent;

    public MessageData()
    {
        mTypeMessage = MESSAGE_TYPE.TEXT_MSG;
        mIDSender = "";
        mDate = "";
        mContent = "";
    }

    public MessageData(MESSAGE_TYPE _type, String _content)
    {
        mTypeMessage = _type;
        mIDSender = FirebaseManager.GetCurrentUserID();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        Date date = new Date();
        mDate = formatter.format(date);
        mContent = _content;
    }
}
