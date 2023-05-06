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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class ViewHelper {

    public static LinearLayout GetLinearLayout(Activity _activity, int _id)
    {
        return (LinearLayout) _activity.findViewById(_id);
    }

    public static void WriteInTextView(TextView _textView, String _toWrite)
    {
        _textView.setText(_toWrite);
    }

    public static EditText GetEditText(Activity _activity, int _id)
    {
        return (EditText) _activity.findViewById(_id);
    }

    public static TextView GetTextView(Activity _activity, int _id)
    {
        return (TextView) _activity.findViewById(_id);
    }
    public static TextView GetTextView(Dialog _dialog, int _id)
    {
        return (TextView) _dialog.findViewById(_id);
    }

    public static Button GetButton(Activity _activity, int _id)
    {
        return (Button) _activity.findViewById(_id);
    }

    public static ImageButton GetImageButton(Activity _activity, int _id)
    {
        return (ImageButton) _activity.findViewById(_id);
    }

    public static CheckBox GetCheckBox(Activity _activity, int _id)
    {
        return (CheckBox) _activity.findViewById(_id);
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
    
    public static LinearLayout CreateCard(Context _context, String _nameCard, float _priceCard, Consumer<View> _callback)
    {
        int _margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, _context.getResources().getDisplayMetrics());

        //Layout
        LinearLayout _layoutCard = new LinearLayout(_context);
        LinearLayout.LayoutParams _paramsLayoutCard = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        _paramsLayoutCard.gravity = Gravity.CENTER;
        _paramsLayoutCard.topMargin = _margin;
        _layoutCard.setLayoutParams(_paramsLayoutCard);
        _layoutCard.setOrientation(LinearLayout.VERTICAL);

        //ImageButton
        ImageButton _imageButton = new ImageButton(_context);
        BindOnClick(_imageButton, _callback);
        int _height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, _context.getResources().getDisplayMetrics());
        _imageButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, _height));
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
        _name.setPadding(_margin/2,0,0,0);
        _name.setTextColor(ContextCompat.getColor(_context, R.color.white));
        _name.setTextSize(18);
        _name.setTypeface(_name.getTypeface(), Typeface.BOLD);
        _cardView.addView(_name);

        //Price
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
