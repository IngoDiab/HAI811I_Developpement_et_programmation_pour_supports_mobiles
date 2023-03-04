package com.mobile.ex2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private List<Sensor> listeCapteur;
    private TextView listeCapteurString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        listeCapteurString = findViewById(R.id.capteurString);
        listeCapteur = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null)
            listeCapteurString.setText(listeCapteurString.getText() + " -> SUCCESS !\n");
        else
            listeCapteurString.setText(listeCapteurString.getText() + " En raison de l'indisponibilté du capteur TYPE_HEART_RATE, l'application SANTÉ ne peut pas fonctionner !\n");

        for (Sensor sensor : listeCapteur)
            if (sensorManager.getDefaultSensor(Sensor.TYPE_ALL) != null)
                listeCapteurString.setText(listeCapteurString.getText() +" NAME : "  + sensor.getName()+" -> SUCCESS !\n");

    }
}