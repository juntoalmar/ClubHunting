package se.kth.mobdev.clubhunting;

import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by eduardogarcia on 26/11/18.
 */

public class Club {

    public String name;
    public LatLng location;
    public String url;
    public String music;
    public float rating;
    public String description;
    public ImageView image;
    Marker marker;

}


