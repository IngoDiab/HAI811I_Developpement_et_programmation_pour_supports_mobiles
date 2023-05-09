package com.mobile.reverleaf;

public class EventData {

    public String mTypeEvent;
    public String mName;
    public String mDescription;
    public String mDate;
    public String mLocation;
    public String mPrice;

    public EventData() {
        this.mTypeEvent = "None";
        this.mName = "None";
        this.mDescription = "None";
        this.mDate = "None";
        this.mLocation = "None";
        this.mPrice = "None";
    }

    public EventData(String _name, String _description, String _date, String _location, String _price) {
        this.mTypeEvent = "None";
        this.mName = _name;
        this.mDescription = _description;
        this.mDate = _date;
        this.mLocation = _location;
        this.mPrice = _price;
    }
}
