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
import android.transition.Scene;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;

import static se.kth.mobdev.clubhunting.MapsActivity.clubs;
import static se.kth.mobdev.clubhunting.MapsActivity.stkLat;
import static se.kth.mobdev.clubhunting.MapsActivity.stkLong;

public class RadarActivity extends AppCompatActivity implements SensorEventListener {

    //private SensorManager mSensorManager;
    //private Sensor mCompass;

    private Button btnGo;
    private TextView tvCompass;
    private RelativeLayout layoutRadar;
    private LinearLayout layoutDetails;

    private int facingClub =0;

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

    public static int RADAR_IMAGE_MIDDLE = 360;
    public static int BUBBLE_SIZE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Clubhunter");
//        setSupportActionBar(toolbar);


        btnGo = (Button) findViewById(R.id.btnGo);
        tvCompass = (TextView) findViewById(R.id.tvCompass);
        layoutRadar = (RelativeLayout) findViewById(R.id.layoutRadar);
        layoutDetails = (LinearLayout) findViewById(R.id.layoutDetails);


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goClick(v);
            }
        });


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


        //Add clubs
     for(int i=0; i<clubs.length; i++)

        {
            //TODO
        //int i=0;
        //

            //Calculate position in the radar
            double deltaX = (clubs[i].location.latitude-stkLat)*10000;
            double deltaY = (stkLong-clubs[i].location.longitude)*10000;


            //Calculate position in the radar
            //TODO
//            deltaX = (-0.05)*10000;
//            deltaY = (0)*10000;
            //TODO

            clubs[i].distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);

//            double minDistance = 120;
//
//            if(distance<minDistance)
//                distance=minDistance;


            //Set position in the layout

            //180dp middle of radar imageview
            //25dp
            int posx = RADAR_IMAGE_MIDDLE+ (int)deltaX -(BUBBLE_SIZE/2);
            int posy = RADAR_IMAGE_MIDDLE+ (int)deltaY -(BUBBLE_SIZE/2);



            //Set bubble size
            double maxSize = 200;
            double minSize = 50;

            double maxRange = 300;
            int size = (int) (maxSize - (clubs[i].distance *(maxSize/maxRange)));

            if(size<minSize)
                size = (int) minSize;
            else if(size>maxSize)
                size = (int) maxSize;


            //Create image
            ImageView bubble = new ImageView(this);

           // Random r = new Random();

            bubble.setImageResource(R.drawable.bubble_red);
            if(size<60)
                bubble.setImageResource(R.drawable.bubble_blue);
            if(size>=60 && size <100)
                bubble.setImageResource(R.drawable.bubble_green);
            if(size>=100 && size <150)
                bubble.setImageResource(R.drawable.bubble_yellow);


//
//            switch(r.nextInt(3))
//            {
//                case 0:
//                    bubble.setImageResource(R.drawable.bubble_red);
//
//                    break;
//                case 1:
//                    bubble.setImageResource(R.drawable.bubble_yellow);
//                    break;
//                case 2:
//                    bubble.setImageResource(R.drawable.bubble_blue);
//                    break;
//                case 3:
//                default:
//                    bubble.setImageResource(R.drawable.bubble_green);
//                    break;
//            }


            //Add details
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(posx, posy, 0, 0);
            lp.width = size;
            lp.height = size;
            bubble.setLayoutParams(lp);

            //TODO
            clubs[i].xRadar = posx;
            clubs[i].yRadar = posy;

            //Calculate angle to center
            double TWOPI = 6.2831853071795865;
            double RAD2DEG = 57.2957795130823209;

            // if (a1 = b1 and a2 = b2) throw an error
            double theta = Math.atan2(posx - RADAR_IMAGE_MIDDLE, posy - RADAR_IMAGE_MIDDLE);
            if (theta < 0.0)
                theta += TWOPI;

            double angle = RAD2DEG * theta;

            clubs[i].radarAzimut = angle * TWOPI / 720;

            //Add to radar
            layoutRadar.addView(bubble);
        }


        //Floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Move to map
                //Intent i;
                //i = new Intent(RadarActivity.this, MapsActivity.class);
                //startActivity(i);

                //TODO silent correct mediaplayer
                //Shut up
                if (clubs[facingClub].mp.isPlaying())
                    clubs[facingClub].mp.pause();

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
            tvCompass = (TextView) findViewById(R.id.tvCompass);
            tvCompass.setText("");
          //  tvCompass.setText("Compass= "+azimuthInRadians );


            //Sound
            //float distance = Math.abs(azimuthInRadians);
            int previous = facingClub;

            facingClub = findClosestClubtoAzimut(azimuthInRadians);




            //Distance to the center of the radar
            float distance = Math.abs( azimuthInRadians - (float)clubs[facingClub].radarAzimut);


            float volume = 1-distance;


            if(facingClub!=previous)
            {
                //Pause previous
                clubs[previous].mp.pause();

                //Play new
                clubs[facingClub].mp.start();
                clubs[facingClub].mp.setVolume(volume, volume);
            }



            //Set the data into the layout
            TextView tvName = (TextView) findViewById(R.id.tvRadarName);
            tvName.setText(clubs[facingClub].name);

            TextView tvKm = (TextView) findViewById(R.id.tvKM);
            tvKm.setText(String.format("%.0f", clubs[facingClub].distance)+"m");



            AlphaAnimation alpha = new AlphaAnimation(volume, volume);
            alpha.setDuration(0); // Make animation instant
            alpha.setFillAfter(true); // Tell it to persist after the animation ends
// And then on your layout

            LinearLayout layoutDetails = (LinearLayout) findViewById(R.id.layoutDetails);
            layoutDetails.startAnimation(alpha);

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

        //TODO silent the proper object
        if(clubs[facingClub].mp.isPlaying())
            clubs[facingClub].mp.pause();

        //COMPASS
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }


    public void goClick(View v) {



        //Move to detail
        Intent i = new Intent(this, DetailsActivity.class);
        //Intent i = new Intent(this, compass.class);

        //TODO: move to the selected club

        i.putExtra("club_selected", facingClub);
        startActivity(i);


    }

    public int findClosestClubtoAzimut(float azimuthInRadians)
    {
        float min = 999999;
        float diff;
        int closest = 0;

        for(int i=0; i<clubs.length;i++)
        {
            diff = Math.abs( azimuthInRadians- (float)(clubs[i].radarAzimut));
            if(diff<min)
            {
                min = diff;
                closest = i;
            }

        }
        return closest;
    }

    //Silents everything
    public void Ssssh()
    {
        for(int i=0;i<clubs.length;i++)
            clubs[i].mp.pause();

    }

}
