package com.mobile.reverleaf;

public class EventData_Sport extends EventData{
    public String mSportName;
    public String mChampionnat;
    public String mDuree;

    public EventData_Sport()
    {
        super();
        this.mTypeEvent = "Sport";
        this.mSportName = "None";
        this.mChampionnat = "None";
        this.mDuree = "None";
    }

    public EventData_Sport(String _name, String _description, String _date, String _location, String _price, String _sportName, String _championnat, String _duree) {
        super(_name, _description, _date, _location, _price);
        this.mTypeEvent = "Sport";
        this.mSportName = _sportName;
        this.mChampionnat = _championnat;
        this.mDuree = _duree;
    }
}
