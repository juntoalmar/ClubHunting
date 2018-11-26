package se.kth.mobdev.clubhunting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



import java.util.Random;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener, OnMapReadyCallback {

    public static double MIN_DISTANCE_PLAY_MUSIC = 0.01;

    private GoogleMap mMap;
    private LatLng[] bars;
    private Club[] clubs;
    private Club closest;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_button);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        //Prova
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        LinearLayout radar = (LinearLayout) findViewById(R.id.bRadar);

        radar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radarClick(v);
            }
        });

// some more code
        //Media player
        mp = MediaPlayer.create(this, R.raw.song);

    }


    public void radarClick(View v) {

        //Move to radar
        Intent i = new Intent(this, RadarActivity.class);
        startActivity(i);




    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Stockholm location
        Double stkLat = 59.3257;
        Double stkLong = 18.065;

        //TODO: get clubs from JSON


        clubs = new Club[10];

        for(int i=0; i< clubs.length; i++)
        {
            Random r = new Random();

            clubs[i] = new Club();

            clubs[i].name = "Club "+i;
            clubs[i].description = "Description "+i;

            //Position based on Stockholm + some random offset
            clubs[i].location = new LatLng(stkLat+((r.nextInt(50)-25)/1000.0),stkLong+((r.nextInt(50)-25)/1000.0));
            //clubs[i].image =

            clubs[i].music = "Music "+i;
            clubs[i].rating = r.nextInt(5);
            clubs[i].url = new String ("https://goo.gl/maps/nsXPnMbUkcS2");



            //Add marker
            //Marker newMark = mMap.addMarker(new MarkerOptions().position(clubs[i].location).title(clubs[i].name));
            clubs[i].marker = mMap.addMarker(new MarkerOptions().position(clubs[i].location).title(clubs[i].name));
            clubs[i].marker.setTag(clubs[i].url);

        }


        /*

        //Ten new places
        bars = new LatLng[10];

        for(int i=0; i< bars.length; i++)
        {
            Random r = new Random();

            //Position based on Stockholm + some random offset
            bars[i] = new LatLng(stkLat+((r.nextInt(50)-25)/1000.0),stkLong+((r.nextInt(50)-25)/1000.0));

            //Add marker
            Marker newMark = mMap.addMarker(new MarkerOptions().position(bars[i]).title("Bar "+i));
            newMark.setTag("https://goo.gl/maps/nsXPnMbUkcS2");

        }
        */

        //Add Stockholm
        //LatLng stockholm = new LatLng(stkLat, stkLong);
        //mMap.addMarker(new MarkerOptions().position(stockholm).title("Stockholm"));



        //Move camera to Stockholm
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(stockholm));
        //mMap.setMinZoomPreference(13.0f);
        //mMap.setMaxZoomPreference(20.0f);

        // Set listeners
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);

    }

    public boolean onMarkerClick(final Marker marker) {


        //Open a bar TEST
        String url = (String) marker.getTag();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
        startActivity(intent);




        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }



    @Override
    public void onCameraMoveStarted(int reason) {
/*
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            Toast.makeText(this, "The user gestured on the map.",
                    Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
            Toast.makeText(this, "The user tapped something on the map.",
                    Toast.LENGTH_SHORT).show();
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
            Toast.makeText(this, "The app moved the camera.",
                    Toast.LENGTH_SHORT).show();
        }
        */
    }

    @Override
    public void onCameraMove() {
        //Toast.makeText(this, "The camera is moving.", Toast.LENGTH_SHORT).show();

        //Get camera position
        CameraPosition camera = mMap.getCameraPosition();
        LatLng location = camera.target;

        double minDistance = 99999999999.9;
        double distance;

        Club previous = closest;



        //Find closest club

        for(int i=0; i<10; i++)
        {
            distance = (Math.sqrt( Math.pow(location.latitude-clubs[i].location.latitude, 2) + Math.pow(location.longitude-clubs[i].location.longitude, 2) ));

            //Closer club
            if(distance<minDistance)
            {
                minDistance = distance;
                closest = clubs[i];
            }
        }

        //Closest club in blue
        closest.marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        closest.marker.showInfoWindow();

        //Change of club
        if((previous!=null) && (previous!=closest))
            previous.marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));




        if(minDistance<MIN_DISTANCE_PLAY_MUSIC) {


            float log1=(float)(Math.log(50-minDistance)/Math.log(50))/10;
            mp.setVolume(1-log1, 1-log1);

            Toast.makeText(this, "Rock and roll!!! "+(1-log1), Toast.LENGTH_SHORT).show();
            //mp.setVolume((float)minDistance, (float)minDistance);

            if(!mp.isPlaying())
                mp.start();
        }
        else
            mp.pause();

    }

    @Override
    public void onCameraMoveCanceled() {
        //Toast.makeText(this, "Camera movement canceled.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraIdle() {
        //Toast.makeText(this, "The camera has stopped moving.", Toast.LENGTH_SHORT).show();
    }
}
