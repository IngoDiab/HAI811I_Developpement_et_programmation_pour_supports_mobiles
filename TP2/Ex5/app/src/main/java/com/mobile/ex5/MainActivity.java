package com.mobile.ex5;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{
    private SensorManager mSensorManager;
    private CameraManager mCameraManager;
    private Sensor mLinearAcceleration;

    private String mBackCameraID = "";
    private boolean mTorchMod = false;
    private long mLastTimeShaken = 0;
    private float mShakeTime = 1000;
    private EditText mSensibility;

    private double mPreviousAcceleration = 0;
    private double mAcceleration = 0;

    protected void GetSensor()
    {
        mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if(mLinearAcceleration != null) return;
        Toast.makeText(MainActivity.this, "Application closed : Sensor Linear Acceleration Needed", Toast.LENGTH_SHORT).show();
        finish();
    }

    protected void GetBackCamera()
    {
        try {
            for (String _id : mCameraManager.getCameraIdList()) {
                CameraCharacteristics _cameraChara = mCameraManager.getCameraCharacteristics(_id);
                boolean _isBackCamera = _cameraChara.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK;
                if (!_isBackCamera) continue;
                boolean _hasFlash = _cameraChara.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                if (!_hasFlash) continue;
                mBackCameraID = _id;
                break;
            }
        }
        catch(CameraAccessException e) {}

        if(!mBackCameraID.equals("")) return;
        Toast.makeText(MainActivity.this, "Application closed : Back Camera Needed", Toast.LENGTH_SHORT).show();
        finish();
    }

    protected void onShakePhone(SensorEvent event)
    {
        //Prevent from detecting to much shake
        long curTime = System.currentTimeMillis();
        if(curTime-mLastTimeShaken < mShakeTime) return;
        mLastTimeShaken = curTime;

        //Get acceleration changement
        float _xMovement = event.values[0];
        float _yMovement = event.values[1];
        float _zMovement = event.values[2];
        mAcceleration = Math.sqrt(_xMovement*_xMovement + _yMovement*_yMovement + _zMovement*_zMovement);
        double _accelerationDelta = Math.abs(mAcceleration-mPreviousAcceleration);
        mPreviousAcceleration = mAcceleration;

        //Get shake sensibility
        int _sensibility = Integer.MAX_VALUE;
        try {
            _sensibility = Integer.parseInt(mSensibility.getText().toString());
        }
        catch(NumberFormatException e) {}

        //If acceleration changement > sensibility, change torch state
        if(_accelerationDelta <= _sensibility) return;
        mTorchMod = !mTorchMod;
        Toast.makeText(MainActivity.this, mTorchMod ? "Torch ON !" : "Torch OFF !", Toast.LENGTH_SHORT).show();
        try {
            mCameraManager.setTorchMode("0", mTorchMod);
        }
        catch(CameraAccessException e) {
            Toast.makeText(MainActivity.this, "Application closed : Back Camera Needed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensibility = (EditText)findViewById(R.id.sensibility);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        GetSensor();

        GetBackCamera();
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {
            case Sensor.TYPE_LINEAR_ACCELERATION:
                onLinearAccelerationChanged(event);
                break;
        }
    }

    private void onLinearAccelerationChanged(SensorEvent event){
        onShakePhone(event);
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