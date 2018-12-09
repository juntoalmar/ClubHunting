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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.List;

import static se.kth.mobdev.clubhunting.MapsActivity.clubs;

public class RadarActivity extends AppCompatActivity implements SensorEventListener {

    //private SensorManager mSensorManager;
    //private Sensor mCompass;

    private TextView tvCompass;
    private RelativeLayout layoutRadar;
    private LinearLayout layoutDetails;

    ////
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Clubhunter");
        setSupportActionBar(toolbar);



        tvCompass = (TextView) findViewById(R.id.tvCompass);
        layoutRadar = (RelativeLayout) findViewById(R.id.layoutRadar);
        layoutDetails = (LinearLayout) findViewById(R.id.layoutDetails);

        //ORIGINAL
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



        //COMPASS
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);



        //Floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Move to map
                //Intent i;
                //i = new Intent(RadarActivity.this, MapsActivity.class);
                //startActivity(i);

                //Shut up
                if (clubs[1].mp.isPlaying())
                    clubs[1].mp.pause();

                finish();


               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
            }
        });
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.


        //ORIGINAL
//        float value = event.values[0];
//        // Do something with this sensor value.
//
//        layoutRadar.setRotation(value);
//
//        tvCompass.setText("Compass="+value);


        //COMPASS
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);

            ra.setFillAfter(true);

            layoutRadar.startAnimation(ra);
            mCurrentDegree = -azimuthInDegress;

           //TODO for prototype
            tvCompass.setText("");
            // tvCompass.setText("Compass= "+azimuthInRadians );


            //Sound
            float distance = Math.abs(azimuthInRadians);

            if(distance<1)
            {

                //layoutRadar.


                float volume = 1-distance;

                if(!clubs[1].mp.isPlaying())
                {
                    clubs[1].mp.start();

                }
                clubs[1].mp.setVolume(volume, volume);

            }
            else
            {
                if(clubs[1].mp.isPlaying())
                    clubs[1].mp.pause();
            }


            //clubs[1].mp.start();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //ORIGINAL
        //mSensorManager.registerListener(this, mCompass, SensorManager.SENSOR_DELAY_NORMAL);

        //COMPASS
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //ORIGINAL
        //mSensorManager.unregisterListener(this);

        //COMPASS
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

}
