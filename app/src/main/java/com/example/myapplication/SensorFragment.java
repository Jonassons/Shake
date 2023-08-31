package com.example.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class SensorFragment extends Fragment {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor gyroscopeSensor;
    private Vibrator vibrator;

    private static final double SHAKE_THRESHOLD = 15.0;

    private TextView accelerometerTextView;
    private TextView gyroscopeTextView;

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                // Uppdatera accelerometerTextView med data
                accelerometerTextView.setText("Accelerometer:\nX: " + x + "\nY: " + y + "\nZ: " + z);

                // Kolla efter skakning
                double acceleration = Math.sqrt(x * x + y * y + z * z);
                if (acceleration > SHAKE_THRESHOLD) {
                    handleShake();
                }
            } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                // Uppdatera gyroscopeTextView med data
                gyroscopeTextView.setText("Gyroscope:\nX: " + x + "\nY: " + y + "\nZ: " + z);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Ignorera
        }
    };

    private void handleShake() {
        // Hantera skakning
        Toast.makeText(requireContext(), "Shake detected!", Toast.LENGTH_SHORT).show();
        if (vibrator != null) {
            vibrator.vibrate(500);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        accelerometerTextView = view.findViewById(R.id.accelerometerTextView);
        gyroscopeTextView = view.findViewById(R.id.gyroscopeTextView);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

        sensorManager.registerListener(sensorListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorListener);
    }
}
