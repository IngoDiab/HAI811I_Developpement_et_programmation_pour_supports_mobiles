package com.mobile.reverleaf;

import java.util.function.Consumer;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
}
