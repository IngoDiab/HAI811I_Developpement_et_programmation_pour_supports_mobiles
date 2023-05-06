package com.mobile.reverleaf;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

    public static void GetCurrentUserData(Consumer<UserData> _userDataCallback)
    {
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if(_currentUser ==  null)
        {
            _userDataCallback.accept(new UserData());
            return;
        }

        mUserReference.child(_currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                UserData _userData = task.getResult().getValue(UserData.class);
                _userDataCallback.accept(_userData);
            }
        });
    }

    public static void GetSubscriptionsData(Consumer<List<AbonnementData>> _subDataCallback) {
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if (_currentUser == null) {
            _subDataCallback.accept(new ArrayList<>());
            return;
        }

        mSubsReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
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
                    }
        );
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
