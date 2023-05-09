package com.mobile.reverleaf;

import java.util.function.Consumer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

    public static void StartNewIntent(Activity _activity, Class _class)
    {
        Intent _intent = new Intent(_activity, _class);
        _activity.startActivity(_intent);
        _activity.finish();
    }

    public static void SwitchFragment(FragmentManager _fragManager, Class _target)
    {
        _fragManager.beginTransaction()
                .addToBackStack("name")
                .replace(R.id.homePage, _target, null)
                .commit();
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
        _paypalButton.setup(PaypalManager.CreatePaymentOrder(_subPrice), PaypalManager.CreateApprovalFeedback(_onPaypalPaid));

        return _subscriptionPopUp;
    }

    public static LinearLayout CreateCard(Context _context, boolean _isMiniCard, String _nameCard, @Nullable Float _priceCard, Consumer<View> _clickCallback)
    {
        int _margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, _context.getResources().getDisplayMetrics());
        int _height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _isMiniCard ? 100 : 160, _context.getResources().getDisplayMetrics());
        int _width = _isMiniCard ? (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, _context.getResources().getDisplayMetrics()) : LinearLayout.LayoutParams.MATCH_PARENT;

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
        _imageButton.setImageDrawable(ContextCompat.getDrawable(_context, R.drawable.abo_bronze));
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
        _name.setText(_nameCard);
        if(_priceCard == null) _name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        _name.setPadding(_margin/2,0,0,0);
        _name.setTextColor(ContextCompat.getColor(_context, R.color.white));
        _name.setTextSize(18);
        _name.setTypeface(_name.getTypeface(), Typeface.BOLD);
        _cardView.addView(_name);

        //Price
        if(_priceCard == null) return _layoutCard;
        TextView _price = new TextView(_context);
        _price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        _price.setText(String.format("%.02f", _priceCard)+"€");
        _price.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        _price.setPadding(0,0,_margin,0);
        _price.setTextColor(ContextCompat.getColor(_context, R.color.white));
        _price.setTextSize(18);
        _price.setTypeface(_price.getTypeface(), Typeface.BOLD_ITALIC);
        _cardView.addView(_price);

        return _layoutCard;
    }
}
