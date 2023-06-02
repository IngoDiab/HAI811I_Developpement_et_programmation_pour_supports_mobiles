package com.mobile.reverleaf;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class FirebaseManager {

    private static final FirebaseAuth mAuthDatabase = FirebaseAuth.getInstance();
    private static final FirebaseDatabase mRootDatabase = FirebaseDatabase.getInstance("https://reverleafdb-default-rtdb.europe-west1.firebasedatabase.app");
    private static final DatabaseReference mUserReference = mRootDatabase.getReference("Users");
    private static final DatabaseReference mSubsReference = mRootDatabase.getReference("Subscription");
    private static final DatabaseReference mEventsReference = mRootDatabase.getReference("Event");
    private static final DatabaseReference mCategoriesReference = mRootDatabase.getReference("Category");
    private static final DatabaseReference mGroupsReference = mRootDatabase.getReference("Groups");
    private static final FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://reverleafdb.appspot.com");
    private static final String mGroupPicturePath = "gs://reverleafdb.appspot.com/Images/Groups/group.png";

    public static FirebaseStorage GetStorage() {return mStorage;}
    public static String GetGroupPicturePath() {return mGroupPicturePath;}

    public static String GetCurrentUserID()
    {
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if(_currentUser ==  null) return "";
        return _currentUser.getUid();
    }

    public static void GetUsernameFromId(String _id, Consumer<String> _onGetName)
    {
        mUserReference.child(_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                UserData _userData = task.getResult().getValue(UserData.class);
                String _name = _userData.mSurname;
                _onGetName.accept(_name.equals("") || _name.equals("None") ? _userData.mMail : _name);
            }
        });
    }

    public static void LoadCurrentUserData(@Nullable Consumer<UserData> _onUserLoaded)
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
                for(String _idEvent : _tagetIDs) {
                    for (DataSnapshot _id : snapshot.getChildren()) {
                        if (!_idEvent.equals(_id.getKey())) continue;
                        try {
                            //Get class corresponding to Data of this EventType
                            Class<?> _classEvent = Class.forName("com.mobile.reverleaf.EventData_" + (String) _id.child("mTypeEvent").getValue());
                            //Load EventData
                            Object _event = _id.getValue(_classEvent);
                            String _nameMethod = "";
                            Class<?>[] _parametersTypes = {};
                            Object[] _parameters = {};

                            switch (_mod) {
                                case MY_EVENTS_CARD:
                                    _nameMethod = "CreateMyEventCard";
                                    _parametersTypes = new Class[]{Activity.class, Resources.class, FragmentManager.class};
                                    _parameters = new Object[]{_activity, _activity.getResources(), _fragManager};
                                    break;

                                case EVENTS_SEARCHED:
                                    _nameMethod = "CreateSearchedCard";
                                    _parametersTypes = new Class[]{Activity.class, Resources.class};
                                    _parameters = new Object[]{_activity, _activity.getResources()};
                                    break;

                                case EVENTS_INSCRIPTIONS_CARD:
                                    _nameMethod = "CreateHomeCard";
                                    _parametersTypes = new Class[]{Activity.class, Boolean.class};
                                    _parameters = new Object[]{_activity, false};
                                    break;

                                case EVENTS_SHARED:

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
                        } catch (ClassNotFoundException e) {
                        } catch (NoSuchMethodException e) {
                        } catch (IllegalAccessException e) {
                        } catch (InvocationTargetException e) {
                        }
                    }
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

    public static void GetCategorie(String _categoryName, Consumer<CategoryData> _categCallback)
    {
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot dsp : snapshot.getChildren())
                {
                    if(!dsp.child("mName").getValue(String.class).equals(_categoryName)) continue;
                    _categCallback.accept(dsp.getValue(CategoryData.class));
                    return;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mCategoriesReference.addListenerForSingleValueEvent(_listener);
    }

    public static void RegisterUser(Activity _activity, UserData _newUser, Consumer<Task<AuthResult>> _successCallback, @Nullable Consumer<Task<AuthResult>> _failCallback)
    {
        mAuthDatabase.createUserWithEmailAndPassword(_newUser.mMail, _newUser.mPassword).addOnCompleteListener(_activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> _task) {
                if (_task.isSuccessful())
                {
                    AddUserToDatabase(_newUser);
                    _successCallback.accept(_task);
                }
                else if(_failCallback != null) _failCallback.accept(_task);
            }
        });
    }

    public static void GoogleSignIn(GoogleSignInAccount _account, Activity _activity, Consumer<Task<AuthResult>> _successCallback)
    {
        AuthCredential _credit = GoogleAuthProvider.getCredential(_account.getIdToken(), null);
        mAuthDatabase.signInWithCredential(_credit).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult)
            {
                 if(!authResult.getAdditionalUserInfo().isNewUser())
                 {
                     _successCallback.accept(null);
                     return;
                 }
                 UserData _userData = new UserData();
                 FirebaseUser _user = mAuthDatabase.getCurrentUser();
                 _userData.mMail = _user.getEmail();
                 _userData.mID = _user.getUid();
                 _userData.mName = _account.getFamilyName();
                 _userData.mPhone = _user.getPhoneNumber();
                AddUserToDatabase(_userData);
                _successCallback.accept(null);
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

    private static ArrayList<TargetWrapper> _targets = new ArrayList<TargetWrapper>();

    public static void LoadImage(View _tag, Context _context, Resources _res, String _path, int _width, int _height, Consumer<Drawable> _onImageLoaded)
    {
        TargetWrapper _target = new TargetWrapper(_context, _res, _onImageLoaded, _path);
        _tag.setTag(_target);

        Picasso _picasso = new Picasso.Builder(_context).addRequestHandler(new StockageRequest()).build();

        if(_width <= 0 && _height <= 0) _picasso.load(_path).into(_target.GetTarget());
        else _picasso.load(_path).resize(_width,_height).into(_target.GetTarget());
    }

    public static void LoadCategoryImage(FORMAT_IMAGE _formatImage, View _tag, Context _context, Resources _res, String _nameCategory, int _width, int _height, Consumer<Drawable> _onImageLoaded)
    {
        GetCategorie(_nameCategory, _category->GetFormattedImagePath(_formatImage, _tag, _context, _res, _category, _width, _height, _onImageLoaded));
    }

    private static void GetFormattedImagePath(FORMAT_IMAGE _formatImage, View _tag, Context _context, Resources _res, CategoryData _category, int _width, int _height, Consumer<Drawable> _onImageLoaded)
    {
        String _pathImage = "";
        switch(_formatImage)
        {

            case SQUARED:
                _pathImage = _category.mPathImageSquare;
                break;

            case BACKGROUND:
            case RECTANGLE:

            default:
                _pathImage = _category.mPathImage;
                break;
        }
        LoadImage(_tag, _context, _res, _pathImage, _width, _height, _onImageLoaded);
    }

    public static void RegisterGroup(String _groupName, Consumer<GroupData> _onGroupCreated)
    {
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if(_currentUser ==  null) return;
        DatabaseReference _refNewEvent = mGroupsReference.push();

        GroupData _group = new GroupData();
        _group.mIDOwner = _currentUser.getUid();
        _group.mID = _refNewEvent.getKey();
        _group.mName = _groupName;
        _group.mIDUsers.add(_group.mIDOwner);

        _refNewEvent.setValue(_group);

        _onGroupCreated.accept(_group);

        AddToListUserDataValue(true, "mIDGroups", _group.mID, null);
    }

    public static void AddUserToGroup(String _idGroup, String _userMail, Consumer<String> _onUserAdded, Consumer<String> _onUserAlreadyIn)
    {
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren())
                {
                    String _mail = dsp.child("mMail").getValue(String.class);
                    if(!_mail.equalsIgnoreCase(_userMail)) continue;
                    String _idUser = dsp.getKey();
                    List<String> _groupsID = new ArrayList<String>();
                    for (DataSnapshot _groupID : dsp.child("mIDGroups").getChildren()) {
                        _groupsID.add(_groupID.getValue(String.class));
                    }
                    if(_groupsID.contains(_idGroup))
                    {
                        _onUserAlreadyIn.accept(_userMail);
                        break;
                    }

                    _onUserAdded.accept(_userMail);
                    _groupsID.add(_idGroup);
                     mUserReference.child(_idUser).child("mIDGroups").setValue(_groupsID);


                    ValueEventListener _groupListener = new ValueEventListener()
                    {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            List<String> _usersID = new ArrayList<String>();
                            for (DataSnapshot _groupID : snapshot.getChildren())
                            {
                                _usersID.add(_groupID.getValue(String.class));
                            }
                            _usersID.add(_idUser);
                            mGroupsReference.child(_idGroup).child("mIDUsers").setValue(_usersID);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    mGroupsReference.child(_idGroup).child("mIDUsers").addListenerForSingleValueEvent(_groupListener);
                    break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mUserReference.addListenerForSingleValueEvent(_listener);
    }

    public static void LoadGroups(Consumer<GroupData> _onGroupFound)
    {
        FirebaseUser _currentUser = mAuthDatabase.getCurrentUser();
        if(_currentUser ==  null) return;
        ValueEventListener _listener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    String _idGroup = dsp.getValue(String.class);

                    ValueEventListener _listenerGroup = new ValueEventListener()
                    {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            GroupData _group = snapshot.getValue(GroupData.class);
                            _onGroupFound.accept(_group);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };
                    mGroupsReference.child(_idGroup).addListenerForSingleValueEvent(_listenerGroup);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mUserReference.child(_currentUser.getUid()).child("mIDGroups").addListenerForSingleValueEvent(_listener);
    }

    public static void SendMessage(String _idGroup, MessageData _message, @Nullable Consumer<MessageData> _onMsgSent)
    {
        ValueEventListener _listener = new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<MessageData> _messages = new ArrayList<>();
                for (DataSnapshot dsp : snapshot.getChildren())
                {
                    _messages.add(dsp.getValue(MessageData.class));
                }
                _messages.add(_message);
                mGroupsReference.child(_idGroup).child("mMessages").setValue(_messages);
                if(_onMsgSent != null) _onMsgSent.accept(_message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mGroupsReference.child(_idGroup).child("mMessages").addListenerForSingleValueEvent(_listener);
    }
}
