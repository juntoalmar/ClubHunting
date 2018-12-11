package se.kth.mobdev.clubhunting;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

    public static double MIN_DISTANCE_PLAY_MUSIC = 0.02;

    private GoogleMap mMap;
    private LatLng[] bars;
    public static Club[] clubs;
    private Club closest;
    private MediaPlayer mp;

    //Stockholm location
    public static Double stkLat = 59.3257;
    public static Double stkLong = 18.065;


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






    }


    @Override
    protected void onResume() {

        super.onResume();

        if(closest!=null)
            if(!closest.mp.isPlaying())
            {

                //TODO comprobacion de distancia
                closest.mp.start();

            }

    }

        public void radarClick(View v) {


        //TODO: add 10 closest clubs and send to radar


        if(closest!=null) {
            if (closest.mp.isPlaying())
                closest.mp.pause();
        }
        //Move to radar
        Intent i = new Intent(this, RadarActivity.class);
            //Intent i = new Intent(this, compass.class);
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
            //clubs[i].marker.setTag(clubs[i].url);
            clubs[i].marker.setTag(i);


            //Media player

            switch (i)
            {
                case 0:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song00);
                    break;

                case 1:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song01);
                    break;

                case 2:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song02);
                    break;

                case 3:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song03);
                    break;

                case 4:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song04);
                    break;

                case 5:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song05);
                    break;

                case 6:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song06);
                    break;

                case 7:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song07);
                    break;

                case 8:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song08);
                    break;

                case 9:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song09);
                    break;

                case 10:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song10);
                    break;

                default:
                    clubs[i].mp = MediaPlayer.create(this, R.raw.song01);
                    break;
            }
            //clubs[i].mp = MediaPlayer.create(this, R.raw.song01);
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
//        String url = (String) marker.getTag();
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
//        startActivity(intent);




        //Open a bar details
        Intent i = new Intent(this, DetailsActivity.class);

        int clubSelected = (int) marker.getTag();
        i.putExtra("club_selected", clubSelected);
        startActivity(i);


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


    public double locationDistance(LatLng loc1, LatLng loc2)
    {
        return Math.sqrt( Math.pow(loc1.latitude-loc2.latitude, 2) + Math.pow(loc1.longitude-loc2.longitude, 2) );
    }

    public Club findClosestClub()
    {
        //Get camera position
        CameraPosition camera = mMap.getCameraPosition();
        LatLng location = camera.target;

        double minDistance = 99999999999.9;
        double distance;

        int i;

       //Find closest club
        for(i=0; i<10; i++)
        {
            //distance = (Math.sqrt( Math.pow(location.latitude-clubs[i].location.latitude, 2) + Math.pow(location.longitude-clubs[i].location.longitude, 2) ));

            distance = locationDistance(location,clubs[i].location );
            //Closer club
            if(distance<minDistance)
            {
                minDistance = distance;
                closest = clubs[i];
            }
        }

        return closest;
    }

    @Override
    public void onCameraMove() {
        //Toast.makeText(this, "The camera is moving.", Toast.LENGTH_SHORT).show();

        //Get camera position
        CameraPosition camera = mMap.getCameraPosition();
        LatLng location = camera.target;

//        double minDistance = 99999999999.9;
//        double distance;

        Club previous = closest;

        closest = findClosestClub();

//        //Find closest club
//
//        for(int i=0; i<10; i++)
//        {
//            distance = (Math.sqrt( Math.pow(location.latitude-clubs[i].location.latitude, 2) + Math.pow(location.longitude-clubs[i].location.longitude, 2) ));
//
//            //Closer club
//            if(distance<minDistance)
//            {
//                minDistance = distance;
//                closest = clubs[i];
//            }
//        }

        //Change
        if(closest != previous)
        {
            //Closest club in blue
            closest.marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            closest.marker.showInfoWindow();

            //Unselect previous
            if(previous!=null) {
                previous.marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                //Unset the music
                previous.mp.pause();
            }

            closest.mp.start();
        }



        //Change of club
//        if((previous!=null) && (previous!=closest))


        double distance = locationDistance(closest.location, location);


        if(distance<MIN_DISTANCE_PLAY_MUSIC) {


            double volume = 1 - (distance/MIN_DISTANCE_PLAY_MUSIC);

//            float log1=(float)(Math.log(50-minDistance)/Math.log(50))/10;
//            mp.setVolume(1-log1, 1-log1);
            closest.mp.setVolume((float)volume, (float)volume);


           // Toast.makeText(this, "Rock and roll!!! "+volume, Toast.LENGTH_SHORT).show();
            //mp.setVolume((float)minDistance, (float)minDistance);

            if(!closest.mp.isPlaying())
                closest.mp.start();
        }
        else
            closest.mp.pause();

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
