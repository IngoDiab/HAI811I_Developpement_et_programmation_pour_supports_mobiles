package com.mobile.reverleaf;

import android.app.Application;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReverleafManager {

    public static void InitializeGooglePlaces(Application _application)
    {
        Places.initialize(_application, "AIzaSyDJcAmHkXXV3x3JRHSpp-XJV4BCJW75-jw");
        PlacesClient placesClient = Places.createClient(_application);
    }

}
