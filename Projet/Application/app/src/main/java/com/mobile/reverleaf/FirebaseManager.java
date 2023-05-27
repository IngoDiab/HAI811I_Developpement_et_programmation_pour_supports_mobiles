package com.mobile.reverleaf;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
                if(_onUserLoaded != null) _onUserLoaded.accept(_userData);
            }
        });
    }

    public static <T> void AddToListUserDataValue(boolean _register, String _listID, T _value, Consumer<T> _onElementAdded)
    {
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if(_currentUser ==  null) return;
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<T> _ids = new ArrayList<T>();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    _ids.add((T)dsp.getValue());
                }
                if(_register) _ids.add(_value);
                else _ids.remove(_value);
                mUserReference.child(_currentUser.getUid()).child(_listID).setValue(_ids);
                if(_onElementAdded!=null) _onElementAdded.accept(_value);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mUserReference.child(_currentUser.getUid()).child(_listID).addListenerForSingleValueEvent(_listener);
    }

    public static void LoadCorrespondingEvents(CARD_MOD _mod, Activity _activity, FragmentManager _fragManager, List<String> _tagetIDs, Consumer<List<LinearLayout>> _getCardsCallback)
    {
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<LinearLayout> _cards = new ArrayList<LinearLayout>();
                for(DataSnapshot _id : snapshot.getChildren()) {
                    if (!_tagetIDs.contains(_id.getKey())) continue;
                    try {
                        //Get class corresponding to Data of this EventType
                        Class<?> _classEvent = Class.forName("com.mobile.reverleaf.EventData_" + (String) _id.child("mTypeEvent").getValue());
                        //Load EventData
                        Object _event = _id.getValue(_classEvent);
                        String _nameMethod = "";
                        Class<?>[] _parametersTypes = {};
                        Object[] _parameters = {};

                        switch(_mod)
                        {
                            case MY_EVENTS_CARD:
                                _nameMethod = "CreateMyEventCard";
                                _parametersTypes = new Class[]{Activity.class, FragmentManager.class};
                                _parameters = new Object[]{_activity, _fragManager};
                                break;

                            case EVENTS_SEARCHED:
                                _nameMethod = "CreateSearchedCard";
                                _parametersTypes = new Class[]{Activity.class};
                                _parameters = new Object[]{_activity};
                                break;

                            case EVENTS_INSCRIPTIONS_CARD:
                                _nameMethod = "CreateHomeCard";
                                _parametersTypes = new Class[]{Activity.class, Boolean.class};
                                _parameters = new Object[]{_activity, false};
                                break;

                            case EVENTS_TENDANCES_CARD:

                            case EVENTS_PROXIMITY_CARD:

                            case EVENTS_FAVORIS_CARD:
                                _nameMethod = "CreateHomeCard";
                                _parametersTypes = new Class[]{Activity.class, Boolean.class};
                                _parameters = new Object[]{_activity, true};
                                break;

                            default:
                                break;
                        }
                        //Get method corresponding to CreateCard
                        Method _createCard = _classEvent.getMethod(_nameMethod, _parametersTypes);
                        //Create card of this eventData
                        LinearLayout _card = (LinearLayout) _createCard.invoke(_event, _parameters);
                        _cards.add(_card);
                    }
                    catch (ClassNotFoundException e) {}
                    catch (NoSuchMethodException e) {}
                    catch (IllegalAccessException e) {}
                    catch (InvocationTargetException e) {}
                }
                _getCardsCallback.accept(_cards);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mEventsReference.addListenerForSingleValueEvent(_listener);
    }

    public static void LoadEventsFromUserList(CARD_MOD _mod, Activity _activity, FragmentManager _fragManager, String _listID, Consumer<List<LinearLayout>> _getCardsCallback)
    {
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if(_currentUser ==  null) return;
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> _ids = new ArrayList<String>();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    _ids.add((String)dsp.getValue());
                }
                LoadCorrespondingEvents(_mod, _activity, _fragManager, _ids, _getCardsCallback);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mUserReference.child(_currentUser.getUid()).child(_listID).addListenerForSingleValueEvent(_listener);
    }

    public static void LoadMostInscriptionEvents(CARD_MOD _mod, Activity _activity, FragmentManager _fragManager, int _nbItemsByCategory, Consumer<List<LinearLayout>> _getCardsCallback)
    {
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> _ids = new ArrayList<String>();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    _ids.add((String)dsp.child("mID").getValue());
                }
                LoadCorrespondingEvents(_mod, _activity, _fragManager, _ids, _getCardsCallback);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mEventsReference.orderByChild("mNbInscrits").limitToFirst(_nbItemsByCategory).addListenerForSingleValueEvent(_listener);
    }

    public static void LoadProximityEvents(CARD_MOD _mod, Activity _activity, FragmentManager _fragManager, Location _userLocation, double _maxDistance, Consumer<List<LinearLayout>> _getCardsCallback)
    {
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> _ids = new ArrayList<String>();
                for (DataSnapshot dsp : snapshot.getChildren())
                {
                    Location _eventLocation = new Location(dsp.child("mID").getValue(String.class) + "_Location");
                    _eventLocation.setLatitude((double)dsp.child("mLocationLatitude").getValue());
                    _eventLocation.setLongitude((double)dsp.child("mLocationLongitude").getValue());
                    float _distanceUserToEvent = _userLocation.distanceTo(_eventLocation);
                    if(_distanceUserToEvent<_maxDistance)
                        _ids.add((String)dsp.child("mID").getValue());
                }
                LoadCorrespondingEvents(_mod, _activity, _fragManager, _ids, _getCardsCallback);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mEventsReference.addListenerForSingleValueEvent(_listener);
    }

    public static void LoadSearchedEvents(CARD_MOD _mod, Activity _activity, FragmentManager _fragManager, SearchData _data, Consumer<List<LinearLayout>> _getCardsCallback)
    {
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> _ids = new ArrayList<String>();
                for (DataSnapshot dsp : snapshot.getChildren())
                {
                    //Title
                    String _title = (String)dsp.child("mName").getValue();
                    boolean _sameTitle = _title.equals("") || _data.mTitle == null || _data.mTitle.equals("") || _data.mTitle.equalsIgnoreCase(_title);
                    if(!_sameTitle) continue;

                    //Category
                    String _category = dsp.child("mTypeEvent").getValue(String.class);
                    boolean _containsCategory = _data.mCategories.isEmpty() || _data.mCategories.contains(_category);
                    if(!_containsCategory) continue;

                    //Lieu
                    String _lieu = dsp.child("mLocation").getValue(String.class);
                    boolean _sameLieu = _data.mLieu.isEmpty() || _data.mLieu.equalsIgnoreCase(_lieu);
                    if(!_sameLieu) continue;

                    //Date
                    boolean _inBetweenDate = false;
                    String _date = dsp.child("mDate").getValue(String.class);
                    SimpleDateFormat _parserDate = new SimpleDateFormat("dd/MM/yyyy");
                    try
                    {
                        Date _dateEventParsed = _parserDate.parse(_date);
                        //True if begin date is not specified or date is same or before
                        boolean _eventIsAfterBegin = _data.mDateBegin.equals("") || _data.mDateBegin.equals(_date) || _parserDate.parse(_data.mDateBegin).before(_dateEventParsed);
                        //True if end date is not specified or date is same or after
                        boolean _eventIsBeforeEnd = _data.mDateEnd.equals("") || _data.mDateEnd.equals(_date) || _parserDate.parse(_data.mDateEnd).after(_dateEventParsed);
                        _inBetweenDate = _eventIsAfterBegin && _eventIsBeforeEnd;
                    } catch (ParseException e) {}
                    if(!_inBetweenDate) continue;

                    //Price
                    Float _price = dsp.child("mPrice").getValue(Float.class);
                    boolean _inBetweenPrice = (_data.mPriceMin == -1 || _data.mPriceMin <= _price) && (_data.mPriceMax == -1 || _data.mPriceMax >= _price);
                    if(!_inBetweenPrice) continue;

                    _ids.add((String)dsp.child("mID").getValue());
                }
                LoadCorrespondingEvents(_mod, _activity, _fragManager, _ids, _getCardsCallback);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mEventsReference.addListenerForSingleValueEvent(_listener);
    }

    public static void IsEventInUserList(String _listName, String _idEvent, Consumer<Boolean> _isInListCallback)
    {
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if(_currentUser ==  null) return;

        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren())
                {
                    if(dsp.getValue(String.class).equals(_idEvent))
                    {
                        _isInListCallback.accept(true);
                        return;
                    }
                }
                _isInListCallback.accept(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mUserReference.child(_currentUser.getUid()).child(_listName).addListenerForSingleValueEvent(_listener);
    }

    public static <T extends EventData> void IncrementEventCounter(String _eventID, String _counterKey, int _incrementation)
    {
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long _nbUsers = (long)snapshot.getValue();
                mEventsReference.child(_eventID).child(_counterKey).setValue(_nbUsers+_incrementation);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mEventsReference.child(_eventID).child(_counterKey).addListenerForSingleValueEvent(_listener);
    }

    public static void ManageInteractionWithEvent(boolean _register, String _eventID, String _listIDUserSide, String _statIDEventSide, Consumer<String> _onElementAdded)
    {
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if(_currentUser ==  null) return;

        AddToListUserDataValue(_register, _listIDUserSide, _eventID, _onElementAdded);
        IncrementEventCounter(_eventID, _statIDEventSide, _register ? 1 : -1);
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
        DatabaseReference _refNewEvent = mEventsReference.push();
        _eventData.mID = _refNewEvent.getKey();
        _refNewEvent.setValue(_eventData);
        FirebaseManager.AddToListUserDataValue(true,"mIDCreatedEvents", _eventData.mID, null);
    }

    public static <T extends EventData> void DeleteEvent(T _eventData)
    {
        //Delete from user
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if(_currentUser ==  null) return;
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> _ids = new ArrayList<String>();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    _ids.add((String)dsp.getValue());
                }
                _ids.remove(_eventData.mID);
                mUserReference.child(_currentUser.getUid()).child("mIDCreatedEvents").setValue(_ids);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mUserReference.child(_currentUser.getUid()).child("mIDCreatedEvents").addListenerForSingleValueEvent(_listener);

        //Delete from events
        mEventsReference.child(_eventData.mID).removeValue();
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
