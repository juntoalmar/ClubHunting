package se.kth.mobdev.clubhunting;

import android.media.MediaPlayer;
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
    public String music_style;
    public float rating;
    public String description;
    public ImageView image;

    Marker marker;

    public MediaPlayer mp;

}


