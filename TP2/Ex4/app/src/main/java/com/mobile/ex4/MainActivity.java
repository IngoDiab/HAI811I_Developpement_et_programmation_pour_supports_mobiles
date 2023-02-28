package com.mobile.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor mLinearAcceleration;
    private TextView mAccelerometerText;
    private float mThreshold = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mAccelerometerText = (TextView)findViewById(R.id.AccelerometerText);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                onAccelerometerChanged(event);
                break;
        }
    }

    private void onAccelerometerChanged(SensorEvent event){
        float _xMovement = event.values[0];
        float _yMovement = event.values[1];

        boolean _movingOnX =  _xMovement < -mThreshold ||  _xMovement > mThreshold;
        boolean _movingOnY =  _yMovement < -mThreshold ||  _yMovement > mThreshold;

        String _msg = "";
        if(_movingOnX) _msg +=  _xMovement < -mThreshold ? "Left" :
                                _xMovement > mThreshold ? "Right" : "";
        if(_movingOnX && _movingOnY) _msg += " & ";
        if(_movingOnY) _msg +=  _yMovement < -mThreshold ? "Down" :
                                _yMovement > mThreshold ? "Up" : "";
        mAccelerometerText.setText(_msg);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}