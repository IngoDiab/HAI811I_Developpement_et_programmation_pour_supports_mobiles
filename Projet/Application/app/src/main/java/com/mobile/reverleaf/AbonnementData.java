package com.mobile.reverleaf;

public class AbonnementData {
    public String mID;
    public String mName;
    public String mDescription;
    public float mPrice;

    public AbonnementData() {
        this.mName = "None";
        this.mDescription = "None";
        this.mPrice = 0;
    }

    public AbonnementData(String _name, String _desc, float _price) {
        this.mName = _name;
        this.mDescription = _desc;
        this.mPrice = _price;
    }

    public AbonnementData(String mID, String _name, String _desc, float _price) {
        this.mID = mID;
        this.mName = _name;
        this.mDescription = _desc;
        this.mPrice = _price;
    }
}
