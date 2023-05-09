package com.mobile.reverleaf;

import android.app.Application;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class ReverleafManager {
    static UserData mCurrentUserData = new UserData();
    static AbonnementData mSubscription = new AbonnementData();

    public static void InitializeGooglePlaces(Application _application)
    {
        Places.initialize(_application, "AIzaSyDJcAmHkXXV3x3JRHSpp-XJV4BCJW75-jw");
        PlacesClient placesClient = Places.createClient(_application);
    }

    public static void SetSubscription(String _nameSub)
    {
        mCurrentUserData.mSubscription = _nameSub;
        FirebaseManager.ChangeUserDataValue("mSubscription", _nameSub);
    }
}
