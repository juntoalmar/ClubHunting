package se.kth.mobdev.clubhunting;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

public class RadarActivity extends AppCompatActivity /*implements SensorEventListener*/ {

//    private SensorManager mSensorManager;
//    private Sensor mCompass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//
//        //Sensors
//        mCompass = null;
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//
//        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//
//        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
//            // Success! There's a compass
//            mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        } else {
//            // Failure! No magnetometer.
//
//            finish();
//        }


        //Floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Move to map
                //Intent i;
                //i = new Intent(RadarActivity.this, MapsActivity.class);
                //startActivity(i);

                finish();


               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
            }
        });
    }

//    @Override
//    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
//        // Do something here if sensor accuracy changes.
//    }
//
//    @Override
//    public final void onSensorChanged(SensorEvent event) {
//        // The light sensor returns a single value.
//        // Many sensors return 3 values, one for each axis.
//        float value = event.values[0];
//        // Do something with this sensor value.
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mSensorManager.registerListener(this, mCompass, SensorManager.SENSOR_DELAY_NORMAL);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mSensorManager.unregisterListener(this);
//    }

}
