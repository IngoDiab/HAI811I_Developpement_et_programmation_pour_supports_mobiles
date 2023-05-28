package com.mobile.reverleaf;

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
        _name.setTextColor(ContextCompat.getColor(_context, R.color.white));
        _name.setTextSize(18);
        _name.setTypeface(_name.getTypeface(), Typeface.BOLD);
        _cardView.addView(_name);

        //Price
        TextView _price = new TextView(_context);
        _price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _price.setText(String.format("%.02f", _subData.mPrice)+"€");
        _price.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        _price.setPadding(0,0,_margin,0);
        _price.setTextColor(ContextCompat.getColor(_context, R.color.white));
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
        _name.setTextColor(ContextCompat.getColor(_context, R.color.white));
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
        _name.setTextColor(ContextCompat.getColor(_context, R.color.white));
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
        _name.setTextColor(ContextCompat.getColor(_context, R.color.white));
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
        _price.setTextColor(ContextCompat.getColor(_context, R.color.white));
        _price.setTextSize(18);
        _price.setTypeface(_price.getTypeface(), Typeface.BOLD_ITALIC);
        _cardView.addView(_price);

        return _layoutCard;
    }
}
