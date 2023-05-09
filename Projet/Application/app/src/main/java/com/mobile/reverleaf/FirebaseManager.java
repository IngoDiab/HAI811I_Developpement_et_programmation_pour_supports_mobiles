package com.mobile.reverleaf;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FirebaseManager {

    private static FirebaseAuth mAuthDatabase = FirebaseAuth.getInstance();
    private static FirebaseDatabase mRootDatabase = FirebaseDatabase.getInstance("https://reverleafdb-default-rtdb.europe-west1.firebasedatabase.app");
    private static DatabaseReference mUserReference = mRootDatabase.getReference("Users");
    private static DatabaseReference mSubsReference = mRootDatabase.getReference("Subscription");
    private static DatabaseReference mEventsReference = mRootDatabase.getReference("Event");
    private static DatabaseReference mCategoriesReference = mRootDatabase.getReference("Category");

    public static void LoadCurrentUserData(@Nullable Consumer<UserData> _onUserLoaded, @Nullable Consumer<AbonnementData> _onSubLoaded)
    {
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if(_currentUser ==  null) return;

        mUserReference.child(_currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                UserData _userData = task.getResult().getValue(UserData.class);
                ReverleafManager.mCurrentUserData = _userData;
                if(_onUserLoaded != null) _onUserLoaded.accept(_userData);

                //mSubsReference.child(_userData.mSubscription).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    //@Override
                    //public void onComplete(@NonNull Task<DataSnapshot> task) {
                        //AbonnementData _userSub = task.getResult().getValue(AbonnementData.class);
                        //ReverleafManager.mSubscription = _userSub;
                        //if(_onSubLoaded != null) _onSubLoaded.accept(_userSub);
                    //}
               //});

            }
        });
    }

    public static <T> void ChangeUserDataValue(String _keyValue, T _value)
    {
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if(_currentUser ==  null) return;
        mUserReference.child(_currentUser.getUid()).child(_keyValue).setValue(_value);
    }

    public static void GetSubscriptionsData(Consumer<List<AbonnementData>> _subDataCallback)
    {
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<AbonnementData> _subsDatas = new ArrayList<>();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    _subsDatas.add(dsp.getValue(AbonnementData.class));
                }
                _subDataCallback.accept(_subsDatas);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mSubsReference.addListenerForSingleValueEvent(_listener);
    }

    public static void GetCategoriesAvailable(Consumer<List<CategoryData>> _categCallback)
    {
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<CategoryData> _categories = new ArrayList<>();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    _categories.add(dsp.getValue(CategoryData.class));
                }
                _categCallback.accept(_categories);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mCategoriesReference.addListenerForSingleValueEvent(_listener);
    }

    public static void RegisterUser(Activity _activity, UserData _newUser, Consumer<Task<AuthResult>> _successCallback, Consumer<Task<AuthResult>> _failCallback)
    {
        mAuthDatabase.createUserWithEmailAndPassword(_newUser.mMail, _newUser.mPassword).addOnCompleteListener(_activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> _task) {
                if (_task.isSuccessful())
                {
                    AddUserToDatabase(_newUser);
                    _successCallback.accept(_task);
                }
                else _failCallback.accept(_task);
            }
        });
    }

    public static <T extends EventData> void RegisterEvent(T _eventData)
    {
        DatabaseReference _refNewEvent = mEventsReference.child(_eventData.mTypeEvent).push();
        _refNewEvent.setValue(_eventData);

        ReverleafManager.AddCreatedEventID(_refNewEvent.getKey());
    }

    private static void AddUserToDatabase(UserData _newUser)
    {
        FirebaseUser _user = mAuthDatabase.getCurrentUser();
        if(_user ==  null) return;
        UserProfileChangeRequest _requestUpdate = new UserProfileChangeRequest.Builder().setDisplayName(_newUser.mSurname).build();
        _user.updateProfile(_requestUpdate);

        mUserReference.child(_user.getUid()).setValue(_newUser);
    }

    public static void ConnectUser(Activity _activity, String _mail, String _password, Consumer<Task<AuthResult>> _successCallback, Consumer<Task<AuthResult>> _failCallback)
    {
        mAuthDatabase.signInWithEmailAndPassword(_mail, _password).addOnCompleteListener(_activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> _task) {
                if (_task.isSuccessful()) _successCallback.accept(_task);
                else _failCallback.accept(_task);
            }
        });
    }

    public static void SignOut()
    {
        mAuthDatabase.signOut();
    }
}
