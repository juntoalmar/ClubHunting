package se.kth.mobdev.clubhunting;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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


        //Ten new places
        LatLng[] bars = new LatLng[10];

        for(int i=0; i< bars.length; i++)
        {
            Random r = new Random();

            //Position based on Stockholm + some random offset
            bars[i] = new LatLng(stkLat+((r.nextInt(50)-25)/1000.0),stkLong+((r.nextInt(50)-25)/1000.0));

            //Add marker
            mMap.addMarker(new MarkerOptions().position(bars[i]).title(""+i));
        }

        //Add Stockholm
        LatLng stockholm = new LatLng(stkLat, stkLong);
        //mMap.addMarker(new MarkerOptions().position(stockholm).title("Stockholm"));



        //Move camera to Stockholm
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stockholm));
        mMap.setMinZoomPreference(13.0f);
        mMap.setMaxZoomPreference(20.0f);

    }
}
