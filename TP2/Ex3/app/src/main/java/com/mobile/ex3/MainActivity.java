package com.mobile.ex3;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor linearAccelerometerSensor;
    private  TextView valeurCapteur;
    private ConstraintLayout mainView;

    private EditText mMinAcc;
    private EditText mMaxAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        valeurCapteur = findViewById(R.id.valueAcc);
        mainView = findViewById(R.id.mainView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        linearAccelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        mMinAcc = (EditText)findViewById(R.id.minAcc);
        mMaxAcc = (EditText)findViewById(R.id.maxAcc);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener( this,linearAccelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);

    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener( this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch(sensorEvent.sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                onLinearAccelerationChanged(sensorEvent);
                break;
        }
    }

    private void onLinearAccelerationChanged(SensorEvent sensorEvent)
    {
        float _xMovement = sensorEvent.values[0];
        float _yMovement = sensorEvent.values[1];
        float _zMovement = sensorEvent.values[2];
        float _acceleration = (float)Math.sqrt(_xMovement*_xMovement + _yMovement*_yMovement + _zMovement*_zMovement);

        float _minAcc = 3;
        float _maxAcc = 6;
        try {
            _minAcc = Float.parseFloat(mMinAcc.getText().toString());
        } catch(NumberFormatException e) {}

        try {
            _maxAcc = Float.parseFloat(mMaxAcc.getText().toString());
        } catch(NumberFormatException e) {}

        valeurCapteur.setText(Float.toString(_acceleration));
        mainView.setBackgroundColor(_acceleration < _minAcc ? Color.GREEN : _acceleration > _maxAcc ? Color.RED : Color.YELLOW);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}