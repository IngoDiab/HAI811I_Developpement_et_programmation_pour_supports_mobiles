package com.mobile.reverleaf;

public class UserData {

    public String mID;
    public String mMail;
    public String mPhone;
    public String mPassword;
    public String mName;
    public String mSurname;
    public String mAddress;
    public String mSubscription;

    public UserData() {
        this.mID = "None";
        this.mMail = "None";
        this.mPhone = "None";
        this.mPassword = "None";
        this.mName = "None";
        this.mSurname = "None";
        this.mAddress = "None";
        this.mSubscription = "None";
    }

    public UserData(String mMail, String mPhone, String mPassword, String mName, String mSurname, String mAddress, String mSubscription) {
        this.mMail = mMail;
        this.mPhone = mPhone;
        this.mPassword = mPassword;
        this.mName = mName;
        this.mSurname = mSurname;
        this.mAddress = mAddress;
        this.mSubscription = mSubscription;
    }

    public UserData(String mID, String mMail, String mPhone, String mPassword, String mName, String mSurname, String mAddress, String mSubscription) {
        this.mID = mID;
        this.mMail = mMail;
        this.mPhone = mPhone;
        this.mPassword = mPassword;
        this.mName = mName;
        this.mSurname = mSurname;
        this.mAddress = mAddress;
        this.mSubscription = mSubscription;
    }
}
