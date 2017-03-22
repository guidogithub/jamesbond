package com.example.gyroscoop;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private TextView status;

    private float[] r0, lastRotation, calibratedRotation, tmp;

    private SensorManager sensorManager;
    private Sensor accelerometer, magneticField;

    private float[] accelerometerReading, magneticFieldReading, calibratedOrientation;

    private static class V3 {
        static void copy(float[] v, float[] src) {
            v[0] = src[0]; v[1] = src[1]; v[2] = src[2];
        }

        static void minus(float[] v, float[] lhs, float[] rhs) {
            v[0] = lhs[0] - rhs[0];
            v[1] = lhs[1] - rhs[1];
            v[2] = lhs[2] - rhs[2];
        }

        static String toString(float[] v) {
            return "[" + v[0] + " " + v[1] + " " + v[2] + "]";
        }
    }

    private static class M9 {
        static float[] copy(float[] m, float[] in) {
            for(int i = 0; i < 9; ++i) m[i] = in[i];
            return m;
        }

        static float[] transpose(float[] m, float[] n) {
            m[0] = n[0]; m[1] = n[3]; m[2] = n[6];
            m[3] = n[1]; m[4] = n[4]; m[5] = n[7];
            m[6] = n[2]; m[7] = n[5]; m[8] = n[8];
            return m;
        }

        static float[] multiply(float[] m, float[] lhs, float[] rhs) {
            for(int i = 0; i < 3; ++i) {
                int i3 = i * 3;
                for (int j = 0; j < 3; ++j) {
                    int ij = i3 + j;
                    m[ij] = 0;
                    for (int k = 0; k < 3; ++k)
                        m[ij] += lhs[i3 + k] + rhs[k * 3 + j];
                }
            }
            return m;
        }

        static float[] identity(float[] m) {
            for(int i = 0; i < 3; ++i)
                for (int j = 0; j < 3; ++j) {
                    if(i == j) m[i*3+j] = 1;
                    else m[i*3+j] = 0;
                }
            return m;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.status = (TextView)findViewById(R.id.textView);

        this.sensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        Button calibrate = (Button)findViewById(R.id.button);
        calibrate.setOnClickListener(this);

        this.r0 = M9.identity(new float[9]);
        this.lastRotation = M9.identity(new float[9]);
        this.calibratedRotation = M9.identity(new float[9]);
        this.tmp = M9.identity(new float[9]);

        this.accelerometerReading = new float[3];
        this.magneticFieldReading = new float[3];
        this.calibratedOrientation = new float[3];

        this.updateView();
    }

    private void doCalibrate() {
        M9.copy(this.r0, this.lastRotation);
    }

    private void calculateCalibratedRotation() {
        M9.multiply(this.calibratedRotation, M9.transpose(this.tmp, this.r0), this.lastRotation);
        SensorManager.getOrientation(this.calibratedRotation, this.calibratedOrientation);
        this.calibratedOrientation[0] *= 180.0 / Math.PI;
        this.calibratedOrientation[1] *= 180.0 / Math.PI;
        this.calibratedOrientation[2] *= 180.0 / Math.PI;
    }

    private void updateView() {
        this.status.setText(V3.toString(this.calibratedOrientation));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            V3.copy(this.accelerometerReading, event.values);
            this.rotationSensorChanged();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            V3.copy(this.magneticFieldReading, event.values);
            this.rotationSensorChanged();
        }
    }

    private void rotationSensorChanged() {
        SensorManager.getRotationMatrix(
            this.lastRotation, null, this.accelerometerReading, this.magneticFieldReading
        );

        this.calculateCalibratedRotation();
        this.updateView();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onResume() {
        super.onResume();
        this.sensorManager.registerListener(this, this.accelerometer,
            SensorManager.SENSOR_DELAY_GAME);
        this.sensorManager.registerListener(this, this.magneticField,
            SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onClick(View v) {
        this.doCalibrate();
    }

}
