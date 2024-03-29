package com.mobile.reverleaf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

public class ViewHelper {

    public static void RefreshView(Activity _activity, @Nullable Bundle _bundle)
    {
        Intent _intent = _activity.getIntent();
        _intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if(_bundle!=null)_intent.putExtras(_bundle);
        _activity.finish();
        _activity.startActivity(_intent);
    }

    public static void PrintToast(Context _context, String _message)
    {
        Toast.makeText(_context, _message, Toast.LENGTH_SHORT).show();
    }

    public static void WriteInTextView(TextView _textView, String _toWrite)
    {
        _textView.setText(_toWrite);
    }

    public static TextView GetTextView(Dialog _dialog, int _id)
    {
        return (TextView) _dialog.findViewById(_id);
    }

    public static <T> T GetViewElement(Activity _activity, int _id)
    {
        return (T) _activity.findViewById(_id);
    }

    public static <T> T GetFragmentElement(FragmentManager _fragManager, int _id)
    {
        return (T) _fragManager.findFragmentById(_id);
    }

    public static <T> T GetViewElement(View _view, int _id)
    {
        return (T) _view.findViewById(_id);
    }

    public static <T> T GetViewElement(Window _window, int _id)
    {
        return (T) _window.findViewById(_id);
    }

    public static void BindOnClick(View _view, Consumer<View> _callback) {
        _view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _callback.accept(view);
            }
        });
    }

    public static void StartNewIntent(Activity _activity, Class _class, boolean _finishCurrentActivity)
    {
        Intent _intent = new Intent(_activity, _class);
        _activity.startActivity(_intent);
        if(_finishCurrentActivity) _activity.finish();
    }

    public static void StartNewIntent(Activity _activity, Class _class, Bundle _bundle, boolean _finishCurrentActivity)
    {
        Intent _intent = new Intent(_activity, _class);
        _intent.putExtras(_bundle);
        _activity.startActivity(_intent);
        if(_finishCurrentActivity) _activity.finish();
    }

    public static void SwitchFragment(FragmentManager _fragManager, Class _target)
    {
        _fragManager.beginTransaction()
                .addToBackStack("name")
                .replace(R.id.homePage, _target, null)
                .commit();
    }

    public static void SwitchFragment(FragmentManager _fragManager, Fragment _target, Bundle _bundle)
    {
        _target.setArguments(_bundle);
        _fragManager.beginTransaction()
                .addToBackStack("name")
                .replace(R.id.homePage, _target, null)
                .commit();
    }

    public static void InitializeFragmentAutoCompletion(AutocompleteSupportFragment _fieldAutoCompletion, List<Place.Field> _dataFocused, Consumer<Place> _onPlaceFound)
    {
        _fieldAutoCompletion.setPlaceFields(_dataFocused);

        _fieldAutoCompletion.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                _onPlaceFound.accept(place);
            }
        });
    }

    public static void OpenCalendar(Context _context, Consumer<String> _onDateChosen)
    {
        DatePickerDialog.OnDateSetListener _dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month+=1;
                String _date = (dayOfMonth<=9 ? "0" : "")+dayOfMonth+(month<=9 ? "/0" : "/")+month+'/'+year;
                _onDateChosen.accept(_date);
            }
        };

        Calendar _calendar = Calendar.getInstance();
        int _year = _calendar.get(Calendar.YEAR);
        int _month = _calendar.get(Calendar.MONTH);
        int _day = _calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog _picker = new DatePickerDialog(_context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, _dateSetListener, _year, _month, _day);
        _picker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        _picker.show();
    }

    public static Dialog OpenPopUp(Context _context, int _idLayoutPopUp)
    {
        Dialog _popup = new Dialog(_context);
        _popup.setContentView(_idLayoutPopUp);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(_popup.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        _popup.show();
        _popup.getWindow().setAttributes(lp);
        _popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return _popup;
    }

    public static Dialog OpenSubscriptionPopUp(Context _context, int _idLayoutPopUp, float _subPrice, Consumer<CaptureOrderResult> _onPaypalPaid)
    {
        Dialog _subscriptionPopUp = OpenPopUp(_context, _idLayoutPopUp);
        PaymentButtonContainer _paypalButton = GetViewElement(_subscriptionPopUp.getWindow(), R.id.payment_button_container);
        _paypalButton.setup(PaypalManager.CreatePaymentOrder(_subPrice), PaypalManager.CreateApprovalFeedback(_onPaypalPaid, _subscriptionPopUp));

        return _subscriptionPopUp;
    }

    public static Dialog OpenAddUserPopUp(Context _context, int _idLayoutPopUp, String _idGroup, Consumer<String> _onUserAdded, Consumer<String> _onUserAlreadyIn)
    {
        Dialog _addUserPopUp = OpenPopUp(_context, _idLayoutPopUp);
        EditText _userMail = ViewHelper.GetViewElement(_addUserPopUp.getWindow(), R.id.userMail);
        Button _addUser = ViewHelper.GetViewElement(_addUserPopUp.getWindow(), R.id.addUser);
        ViewHelper.BindOnClick(_addUser, _view->FirebaseManager.AddUserToGroup(_idGroup, _userMail.getText().toString(), _onUserAdded, _onUserAlreadyIn));

        return _addUserPopUp;
    }

    public static Dialog OpenShareEventPopUp(Context _context, int _idLayoutPopUp, String _eventIDToShare)
    {
        Dialog _shareEventPopUp = OpenPopUp(_context, _idLayoutPopUp);
        LinearLayout _listGroup = ViewHelper.GetViewElement(_shareEventPopUp.getWindow(), R.id.group_list);
        FirebaseManager.LoadGroups(_group->{
            MessageData _message = new MessageData(MESSAGE_TYPE.EVENT_SHARED_MSG, _eventIDToShare);
            LinearLayout _groupLayout = CreateShareGroupsInfo(_context, _group, _groupShared->FirebaseManager.SendMessage(_groupShared.mID, _message, null));
            _listGroup.addView(_groupLayout);
        });

        return _shareEventPopUp;
    }

    public static void DisplayCardsLoadedEvents(LinearLayout _globalView, List<LinearLayout> _myCards)
    {
        for(LinearLayout _card : _myCards)
            _globalView.addView(_card);
    }

    public static LinearLayout CreateSubCard(Context _context, Resources _resources, AbonnementData _subData, Consumer<View> _clickCallback)
    {
        int _margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, _context.getResources().getDisplayMetrics());
        int _height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, _context.getResources().getDisplayMetrics());
        int _width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, _context.getResources().getDisplayMetrics());

        //Layout
        LinearLayout _layoutCard = new LinearLayout(_context);
        LinearLayout.LayoutParams _paramsLayoutCard = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsLayoutCard.gravity = Gravity.CENTER;
        _paramsLayoutCard.topMargin = _margin;
        _layoutCard.setLayoutParams(_paramsLayoutCard);
        _layoutCard.setOrientation(LinearLayout.VERTICAL);

        //ImageButton
        ImageButton _imageButton = new ImageButton(_context);
        BindOnClick(_imageButton, _clickCallback);
        _imageButton.setLayoutParams(new LinearLayout.LayoutParams(_width, _height));
        _imageButton.setBackgroundColor(Color.TRANSPARENT);
        FirebaseManager.LoadImage(_imageButton, _context, _resources, _subData.mPathImage, _width, _height, _drawable->_imageButton.setImageDrawable(_drawable));
        _layoutCard.addView(_imageButton);


        //Card
        CardView _cardView = new CardView(_context);
        LinearLayout.LayoutParams _paramsCardView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsCardView.topMargin = -_margin;
        _cardView.setLayoutParams(_paramsCardView);
        _cardView.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.mainColor));
        _cardView.setRadius(3);
        _cardView.setCardElevation(4);
        _layoutCard.addView(_cardView);

        //Name
        TextView _name = new TextView(_context);
        _name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _name.setText(_subData.mName);
        _name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        _name.setPadding(_margin/2,0,0,0);
        _name.setTextColor(ContextCompat.getColor(_context, R.color.cardBoard));
        _name.setTextSize(18);
        _name.setTypeface(_name.getTypeface(), Typeface.BOLD);
        _cardView.addView(_name);

        //Price
        TextView _price = new TextView(_context);
        _price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _price.setText(String.format("%.02f", _subData.mPrice)+"€");
        _price.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        _price.setPadding(0,0,_margin,0);
        _price.setTextColor(ContextCompat.getColor(_context, R.color.cardBoard));
        _price.setTextSize(18);
        _price.setTypeface(_price.getTypeface(), Typeface.BOLD_ITALIC);
        _cardView.addView(_price);

        return _layoutCard;
    }

    public static LinearLayout CreateMyEventCard(Context _context, Resources _resources, String _nameCategory, String _nameCard, Consumer<View> _clickCallback, @Nullable Consumer<View> _deleteCallback)
    {
        int _heightDelete = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, _context.getResources().getDisplayMetrics());
        int _margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, _context.getResources().getDisplayMetrics());
        int _height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, _context.getResources().getDisplayMetrics());
        int _width = LinearLayout.LayoutParams.MATCH_PARENT;

        //Layout
        LinearLayout _layoutCard = new LinearLayout(_context);
        LinearLayout.LayoutParams _paramsLayoutCard = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsLayoutCard.gravity = Gravity.CENTER;
        _paramsLayoutCard.topMargin = _margin;
        _layoutCard.setLayoutParams(_paramsLayoutCard);
        _layoutCard.setOrientation(LinearLayout.VERTICAL);

        //DeleteButton
        if(_deleteCallback != null)
        {
            ImageButton _deleteButton = new ImageButton(_context);
            BindOnClick(_deleteButton, _deleteCallback);
            LinearLayout.LayoutParams _deleteLayoutCard = new LinearLayout.LayoutParams(_width, _heightDelete);
            _deleteLayoutCard.bottomMargin = -_margin;
            _deleteButton.setLayoutParams(_deleteLayoutCard);
            _deleteButton.setBackgroundColor(Color.TRANSPARENT);
            _deleteButton.setImageDrawable(ContextCompat.getDrawable(_context, R.drawable.icon_delete));
            _layoutCard.addView(_deleteButton);
        }

        //ImageButton
        ImageButton _imageButton = new ImageButton(_context);
        BindOnClick(_imageButton, _clickCallback);
        _imageButton.setLayoutParams(new LinearLayout.LayoutParams(_width, _height));
        _imageButton.setBackgroundColor(Color.TRANSPARENT);
        _layoutCard.addView(_imageButton);
        FirebaseManager.LoadCategoryImage(FORMAT_IMAGE.RECTANGLE, _imageButton, _context, _resources, _nameCategory, 0,0, _drawable->_imageButton.setImageDrawable(_drawable));

        //Card
        CardView _cardView = new CardView(_context);
        LinearLayout.LayoutParams _paramsCardView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsCardView.topMargin = -_margin;
        _cardView.setLayoutParams(_paramsCardView);
        _cardView.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.mainColor));
        _cardView.setRadius(3);
        _cardView.setCardElevation(4);
        _layoutCard.addView(_cardView);

        //Name
        TextView _name = new TextView(_context);
        _name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _name.setText(_nameCard);
        _name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        _name.setPadding(_margin/2,0,0,0);
        _name.setTextColor(ContextCompat.getColor(_context, R.color.cardBoard));
        _name.setTextSize(18);
        _name.setTypeface(_name.getTypeface(), Typeface.BOLD);
        _cardView.addView(_name);

        //_deleteButton.bringToFront();
        //_layoutCard.requestLayout();
        //_layoutCard.invalidate();

        return _layoutCard;
    }

    public static LinearLayout CreateCategoryCard(Context _context, Resources _resources, String _nameCategory, Consumer<View> _clickCallback)
    {
        int _margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, _context.getResources().getDisplayMetrics());
        int _height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, _context.getResources().getDisplayMetrics());
        int _width = LinearLayout.LayoutParams.MATCH_PARENT;

        //Layout
        LinearLayout _layoutCard = new LinearLayout(_context);
        LinearLayout.LayoutParams _paramsLayoutCard = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsLayoutCard.gravity = Gravity.CENTER;
        _paramsLayoutCard.topMargin = _margin;
        _layoutCard.setLayoutParams(_paramsLayoutCard);
        _layoutCard.setOrientation(LinearLayout.VERTICAL);

        //ImageButton
        ImageButton _imageButton = new ImageButton(_context);
        BindOnClick(_imageButton, _clickCallback);
        _imageButton.setLayoutParams(new LinearLayout.LayoutParams(_width, _height));
        _imageButton.setBackgroundColor(Color.TRANSPARENT);
        _layoutCard.addView(_imageButton);
        FirebaseManager.LoadCategoryImage(FORMAT_IMAGE.RECTANGLE, _imageButton, _context, _resources, _nameCategory, 0,0, _drawable->_imageButton.setImageDrawable(_drawable));

        //Card
        CardView _cardView = new CardView(_context);
        LinearLayout.LayoutParams _paramsCardView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsCardView.topMargin = -_margin;
        _cardView.setLayoutParams(_paramsCardView);
        _cardView.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.mainColor));
        _cardView.setRadius(3);
        _cardView.setCardElevation(4);
        _layoutCard.addView(_cardView);

        //Name
        TextView _name = new TextView(_context);
        _name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _name.setText(_nameCategory);
        _name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        _name.setPadding(_margin/2,0,0,0);
        _name.setTextColor(ContextCompat.getColor(_context, R.color.cardBoard));
        _name.setTextSize(18);
        _name.setTypeface(_name.getTypeface(), Typeface.BOLD);
        _cardView.addView(_name);

        return _layoutCard;
    }

    public static LinearLayout CreateHomeCard(Context _context, Resources _resources, String _nameCard, String _nameCategory, @Nullable Float _priceCard, Consumer<View> _clickCallback)
    {
        int _marginTopLayout = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, _context.getResources().getDisplayMetrics());
        int _marginLeftLayout = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, _context.getResources().getDisplayMetrics());
        int _height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, _context.getResources().getDisplayMetrics());
        int _width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, _context.getResources().getDisplayMetrics());

        //Layout
        LinearLayout _layoutCard = new LinearLayout(_context);
        LinearLayout.LayoutParams _paramsLayoutCard = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsLayoutCard.gravity = Gravity.CENTER;
        _paramsLayoutCard.topMargin = _marginTopLayout;
        _paramsLayoutCard.rightMargin = _marginLeftLayout;
        _layoutCard.setLayoutParams(_paramsLayoutCard);
        _layoutCard.setOrientation(LinearLayout.VERTICAL);

        //ImageButton
        ImageButton _imageButton = new ImageButton(_context);
        BindOnClick(_imageButton, _clickCallback);
        _imageButton.setLayoutParams(new LinearLayout.LayoutParams(_width, _height));
        _imageButton.setBackgroundColor(Color.TRANSPARENT);
        _layoutCard.addView(_imageButton);
        FirebaseManager.LoadCategoryImage(FORMAT_IMAGE.SQUARED, _imageButton, _context, _resources, _nameCategory, _width,_height, _drawable->_imageButton.setImageDrawable(_drawable));

        //Card
        CardView _cardView = new CardView(_context);
        LinearLayout.LayoutParams _paramsCardView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsCardView.topMargin = -_marginTopLayout;
        _cardView.setLayoutParams(_paramsCardView);
        _cardView.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.mainColor));
        _cardView.setRadius(3);
        _cardView.setCardElevation(4);
        _layoutCard.addView(_cardView);

        //Name
        TextView _name = new TextView(_context);
        _name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _name.setText(_nameCard);
        if(_priceCard == null) _name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        _name.setPadding(_marginTopLayout/2,0,0,0);
        _name.setTextColor(ContextCompat.getColor(_context, R.color.cardBoard));
        _name.setTextSize(18);
        _name.setTypeface(_name.getTypeface(), Typeface.BOLD);
        _cardView.addView(_name);

        if(_priceCard == null) return _layoutCard;
        //Price
        TextView _price = new TextView(_context);
        _price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _price.setText(String.format("%.02f", _priceCard)+"€");
        _price.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        _price.setPadding(0,0,_marginTopLayout,0);
        _price.setTextColor(ContextCompat.getColor(_context, R.color.cardBoard));
        _price.setTextSize(18);
        _price.setTypeface(_price.getTypeface(), Typeface.BOLD_ITALIC);
        _cardView.addView(_price);

        return _layoutCard;
    }

    public static LinearLayout CreateGroupCard(Context _context, Resources _resources, String _nameCard, Consumer<View> _clickCallback)
    {
        int _margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, _context.getResources().getDisplayMetrics());
        int _height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, _context.getResources().getDisplayMetrics());
        int _width = LinearLayout.LayoutParams.MATCH_PARENT;

        //Layout
        LinearLayout _layoutCard = new LinearLayout(_context);
        LinearLayout.LayoutParams _paramsLayoutCard = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsLayoutCard.gravity = Gravity.CENTER;
        _paramsLayoutCard.topMargin = _margin;
        _layoutCard.setLayoutParams(_paramsLayoutCard);
        _layoutCard.setOrientation(LinearLayout.VERTICAL);

        //ImageButton
        ImageButton _imageButton = new ImageButton(_context);
        BindOnClick(_imageButton, _clickCallback);
        _imageButton.setLayoutParams(new LinearLayout.LayoutParams(_width, _height));
        _imageButton.setBackgroundColor(Color.TRANSPARENT);
        _layoutCard.addView(_imageButton);
        FirebaseManager.LoadImage(_imageButton, _context, _resources, FirebaseManager.GetGroupPicturePath(), 0,0, _drawable->_imageButton.setImageDrawable(_drawable));

        //Card
        CardView _cardView = new CardView(_context);
        LinearLayout.LayoutParams _paramsCardView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsCardView.topMargin = -_margin;
        _cardView.setLayoutParams(_paramsCardView);
        _cardView.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.mainColor));
        _cardView.setRadius(3);
        _cardView.setCardElevation(4);
        _layoutCard.addView(_cardView);

        //Name
        TextView _name = new TextView(_context);
        _name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _name.setText(_nameCard);
        _name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        _name.setPadding(_margin/2,0,0,0);
        _name.setTextColor(ContextCompat.getColor(_context, R.color.cardBoard));
        _name.setTextSize(18);
        _name.setTypeface(_name.getTypeface(), Typeface.BOLD);
        _cardView.addView(_name);

        return _layoutCard;
    }

    public static LinearLayout CreateMessage(Context _context, MessageData _message)
    {
        int _marginLittleSide = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, _context.getResources().getDisplayMetrics());
        int _marginBigSide = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, _context.getResources().getDisplayMetrics());
        boolean _isUserOwner = _message.mIDSender.equalsIgnoreCase(FirebaseManager.GetCurrentUserID());

        //Layout
        LinearLayout _layoutMessage = new LinearLayout(_context);
        LinearLayout.LayoutParams _paramsLayoutMsg = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsLayoutMsg.topMargin = _marginLittleSide;
        _paramsLayoutMsg.leftMargin = _isUserOwner ? _marginBigSide : _marginLittleSide;
        _paramsLayoutMsg.rightMargin = _isUserOwner ? _marginLittleSide : _marginBigSide;
        _layoutMessage.setLayoutParams(_paramsLayoutMsg);
        if(_isUserOwner) _layoutMessage.setGravity(Gravity.RIGHT);
        _layoutMessage.setOrientation(LinearLayout.VERTICAL);

        if(!_isUserOwner) {
            //Name
            TextView _nameText = new TextView(_context);
            _nameText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            FirebaseManager.GetUsernameFromId(_message.mIDSender, _name -> _nameText.setText(_name));
            _nameText.setTextColor(ContextCompat.getColor(_context, R.color.cardBoard));
            _nameText.setTextSize(12);
            _nameText.setTypeface(_nameText.getTypeface(), Typeface.BOLD);
            _layoutMessage.addView(_nameText);
        }

        //Msg
        TextView _msgText = new TextView(_context);
        _msgText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        _msgText.setText(_message.mContent);
        _msgText.setTextColor(ContextCompat.getColor(_context, _isUserOwner ? R.color.cardBoard : R.color.mainColor));
        _msgText.setBackgroundColor(ContextCompat.getColor(_context, _isUserOwner ? R.color.clickedColor : R.color.cardBoard));
        _msgText.setPadding(10,0,10,0);
        _msgText.setTextSize(18);
        _layoutMessage.addView(_msgText);

        //Date
        TextView _dateText = new TextView(_context);
        _dateText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        _dateText.setText(_message.mDate);
        _dateText.setTextColor(ContextCompat.getColor(_context, R.color.cardBoard));
        _dateText.setTextSize(12);
        _dateText.setTypeface(_dateText.getTypeface(), Typeface.BOLD);
        _layoutMessage.addView(_dateText);

        return _layoutMessage;
    }

    public static List<LinearLayout> CreateMessagesSharedEvent(Context _context, List<LinearLayout> _events, List<MessageData> _messagesList)
    {
        int _marginLittleSide = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, _context.getResources().getDisplayMetrics());
        int _marginBigSide = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, _context.getResources().getDisplayMetrics());
        String _currentUserID = FirebaseManager.GetCurrentUserID();

        List<LinearLayout> _encapsulatedEvent = new ArrayList<>();
        for(int i=0; i<_events.size(); ++i)
        {
            LinearLayout _cardEvent = _events.get(i);
            MessageData _message = _messagesList.get(i);
            boolean _isUserOwner = _message.mIDSender.equalsIgnoreCase(_currentUserID);

            //Layout
            LinearLayout _layoutMessage = new LinearLayout(_context);
            LinearLayout.LayoutParams _paramsLayoutMsg = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            _paramsLayoutMsg.topMargin = _marginLittleSide;
            _paramsLayoutMsg.leftMargin = _isUserOwner ? _marginBigSide : _marginLittleSide;
            _paramsLayoutMsg.rightMargin = _isUserOwner ? _marginLittleSide : _marginBigSide;
            _layoutMessage.setLayoutParams(_paramsLayoutMsg);
            if (_isUserOwner) _layoutMessage.setGravity(Gravity.RIGHT);
            _layoutMessage.setOrientation(LinearLayout.VERTICAL);

            if (!_isUserOwner) {
                //Name
                TextView _nameText = new TextView(_context);
                _nameText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                FirebaseManager.GetUsernameFromId(_message.mIDSender, _name -> _nameText.setText(_name));
                _nameText.setTextColor(ContextCompat.getColor(_context, R.color.cardBoard));
                _nameText.setTextSize(12);
                _nameText.setTypeface(_nameText.getTypeface(), Typeface.BOLD);
                _layoutMessage.addView(_nameText);
            }

            //Event
            LinearLayout.LayoutParams _paramsLayoutCard = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            if (_isUserOwner) _layoutMessage.setGravity(Gravity.RIGHT);
            _cardEvent.setLayoutParams(_paramsLayoutCard);
            _layoutMessage.addView(_cardEvent);

            //Date
            TextView _dateText = new TextView(_context);
            _dateText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            _dateText.setText(_message.mDate);
            _dateText.setTextColor(ContextCompat.getColor(_context, R.color.cardBoard));
            _dateText.setTextSize(12);
            _dateText.setTypeface(_dateText.getTypeface(), Typeface.BOLD);
            _layoutMessage.addView(_dateText);

            _encapsulatedEvent.add(_layoutMessage);
        }

        return _encapsulatedEvent;
    }

    private static LinearLayout CreateShareGroupsInfo(Context _context, GroupData _group, Consumer<GroupData> _onShareGroupClicked)
    {
        int _paddingBottom = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, _context.getResources().getDisplayMetrics());
        int _height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, _context.getResources().getDisplayMetrics());

        //Layout
        LinearLayout _layoutGroup = new LinearLayout(_context);
        LinearLayout.LayoutParams _paramsLayoutMsg = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _layoutGroup.setLayoutParams(_paramsLayoutMsg);
        _layoutGroup.setPadding(0,0,0,_paddingBottom);
        _layoutGroup.setOrientation(LinearLayout.VERTICAL);

        //Card
        CardView _cardView = new CardView(_context);
        LinearLayout.LayoutParams _paramsCardView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, _height);
        _paramsCardView.bottomMargin = _paddingBottom/2;
        _cardView.setLayoutParams(_paramsCardView);
        _cardView.setCardBackgroundColor(ContextCompat.getColor(_context, R.color.cardBoard));
        _cardView.setRadius(3);
        _cardView.setCardElevation(4);
        _layoutGroup.addView(_cardView);

        //Msg
        TextView _msgText = new TextView(_context);
        _msgText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _msgText.setText(_group.mName);
        _msgText.setTextColor(ContextCompat.getColor(_context, R.color.mainColor));
        _msgText.setBackgroundColor(ContextCompat.getColor(_context, R.color.cardBoard));
        _msgText.setPadding(10,0,10,0);
        _msgText.setTextSize(24);
        _msgText.setTypeface(_msgText.getTypeface(), Typeface.BOLD);
        _msgText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        _msgText.setGravity(Gravity.CENTER);
        _cardView.addView(_msgText);

        //Button Share
        Button _button = new Button(_context);
        _button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        _button.setTextColor(ContextCompat.getColor(_context, R.color.mainColor));
        _button.setBackgroundColor(ContextCompat.getColor(_context, R.color.cardBoard));
        _button.setText("PARTAGER");
        _layoutGroup.addView(_button);
        BindOnClick(_button, _view->_onShareGroupClicked.accept(_group));

        return _layoutGroup;
    }

    public static LinearLayout CreateGroupNotif(Context _context, MessageData _notif)
    {
        int _margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, _context.getResources().getDisplayMetrics());

        //Layout
        LinearLayout _layoutNotif = new LinearLayout(_context);
        LinearLayout.LayoutParams _paramsLayoutNotif = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        _paramsLayoutNotif.topMargin = _margin;
        _layoutNotif.setLayoutParams(_paramsLayoutNotif);
        _layoutNotif.setPadding(0,0,0,_margin);
        _layoutNotif.setOrientation(LinearLayout.VERTICAL);

        //Msg
        TextView _msgText = new TextView(_context);
        _msgText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _msgText.setText(_notif.mContent);
        _msgText.setTextColor(ContextCompat.getColor(_context, R.color.grayColor));
        _msgText.setBackgroundColor(ContextCompat.getColor(_context, R.color.blackTransparent));
        _msgText.setTypeface(_msgText.getTypeface(), Typeface.BOLD);
        _msgText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        _layoutNotif.addView(_msgText);

        return _layoutNotif;
    }
}
