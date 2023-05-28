package com.mobile.reverleaf;

public class AbonnementData {
    public String mID;
    public String mName;
    public String mDescription;
    public float mPrice;
    public String mPathImage;

    public AbonnementData() {
        this.mName = "None";
        this.mDescription = "None";
        this.mPrice = 0;
        this.mPathImage = "None";
    }

    public AbonnementData(String _name, String _desc, float _price, String _path) {
        this.mName = _name;
        this.mDescription = _desc;
        this.mPrice = _price;
        this.mPathImage = _path;
    }

    public AbonnementData(String mID, String _name, String _desc, float _price, String _path) {
        this.mID = mID;
        this.mName = _name;
        this.mDescription = _desc;
        this.mPrice = _price;
        this.mPathImage = _path;
    }
}
