package com.mobile.ex6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private ImageView mProxFeedback;
    private EditText mProxMin;
    private EditText mProxMax;

    protected void GetSensor()
    {
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(mProximity != null) return;
        Toast.makeText(MainActivity.this, "Application closed : Sensor Proximity Needed", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProxFeedback = (ImageView) findViewById(R.id.proxFeedback);
        mProxMin = (EditText)findViewById(R.id.minProx);
        mProxMax = (EditText)findViewById(R.id.maxProx);

        GetSensor();
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {
            case Sensor.TYPE_PROXIMITY:
                onProximityChanged(event);
                break;
        }
    }

    private void onProximityChanged(SensorEvent event){
        float _distance = event.values[0];

        int _minDistance = 0;
        int _maxDistance = 100;
        try {
            _minDistance = Integer.parseInt(mProxMin.getText().toString());
        } catch(NumberFormatException e) {}

        try {
            _maxDistance = Integer.parseInt(mProxMax.getText().toString());
        } catch(NumberFormatException e) {}

        if(_distance <= _minDistance) mProxFeedback.setImageResource(R.mipmap.ic_closeprox_foreground);
        else if(_distance >= _maxDistance) mProxFeedback.setImageResource(R.mipmap.ic_farprox_foreground);
        else mProxFeedback.setImageResource(R.mipmap.ic_goodprox_foreground);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}