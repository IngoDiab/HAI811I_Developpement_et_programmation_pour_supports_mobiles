package com.mobile.ex1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private List<Sensor> listeCapteur;
    private TextView listeCapteurString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        listeCapteurString = findViewById(R.id.capteurString);
        listeCapteur = sensorManager.getSensorList(Sensor.TYPE_ALL);
        getCapteur();

    }
    @SuppressLint("SetTextI18n")
    private void getCapteur() {
        int i =1;
        for (Sensor s : listeCapteur) {

            listeCapteurString.setText(listeCapteurString.getText() + "\n" +
                    i+")"+ "Nom du capteur  = " + s.getName() + "\n" +
                    "Type du capteur = " + s.getType() + "\n" +
                    "Version du capteur = " + s.getVersion() + "\n" +
                    "Resolution (in the sensor unit): " +
                    s.getResolution() + "\r\n" +
                    "Power in mA used by this sensor while in use" +
                    s.getPower() + "\r\n" +
                    "Vendor: " + s.getVendor() + "\r\n" +
                    "Maximum range of the sensor in the sensor's unit." +
                    s.getMaximumRange() + "\r\n" +
                    "Minimum delay allowed between two events in microsecond »" + " or zero if this sensor only returns a value when the data its measuring changes »" + s.getMinDelay() + "\r\n");
            i++;
        }
    }
}