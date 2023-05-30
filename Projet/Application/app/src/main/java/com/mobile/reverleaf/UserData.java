package com.mobile.reverleaf;

import java.util.ArrayList;
import java.util.List;

public class UserData {

    public String mID;
    public String mMail;
    public String mPhone;
    public String mPassword;
    public String mName;
    public String mSurname;
    public String mAddress;
    public String mSubscription;

    public List<String> mIDCreatedEvents;
    public List<String> mIDInscritEvents;
    public List<String> mIDFavorisEvents;
    public List<String> mIDGroups;

    public UserData() {
        mIDCreatedEvents = new ArrayList<>();
        mIDInscritEvents = new ArrayList<>();
        mIDFavorisEvents = new ArrayList<>();
        mIDGroups = new ArrayList<>();
        this.mID = "None";
        this.mMail = "None";
        this.mPhone = "None";
        this.mPassword = "None";
        this.mName = "None";
        this.mSurname = "None";
        this.mAddress = "None";
        this.mSubscription = "None";
    }

    public UserData(String _mail, String _phone, String _password, String _name, String _surname, String _address, String _subscription) {
        mIDCreatedEvents = new ArrayList<>();
        mIDInscritEvents = new ArrayList<>();
        mIDFavorisEvents = new ArrayList<>();
        mIDGroups = new ArrayList<>();
        this.mMail = _mail;
        this.mPhone = _phone;
        this.mPassword = _password;
        this.mName = _name;
        this.mSurname = _surname;
        this.mAddress = _address;
        this.mSubscription = _subscription;
    }
}
