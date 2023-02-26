package com.mobile.ex8;

import java.util.ArrayList;

public class Repository {

    private ArrayList<Trip> trips;

    public Repository()
    {
        this.trips = new ArrayList<Trip>();

        trips.add(new Trip("Paris","Lyon","12h30", "14h35","23"));
        trips.add(new Trip("Paris","Lyon","19h57", "21h28","100"));
    }
    public ArrayList getTrips(){return trips;}
}