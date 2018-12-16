package se.kth.mobdev.clubhunting;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import 	android.content.Intent;


public class DetailsActivity extends AppCompatActivity {

    Club c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("       ");
//        setSupportActionBar(toolbar);

        ImageButton imgBtnBack = (ImageButton) findViewById(R.id.imgBtnBack);



        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgbtnBackClick(v);
            }
        });



        Bundle bundle = getIntent().getExtras();

        if(bundle==null)
        {
            //No club
            c = null;

        }
        else
        {
            int selected = bundle.getInt("club_selected", -1);

            if(selected==-1)
                c = null;
            else
                c = MapsActivity.clubs[selected];
        }


        if(c!=null)
        {
            if(c.image!=null)
            {
                ImageView image = (ImageView) findViewById(R.id.clubImage);
                image = c.image;
            }

            RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
            ratingBar.setRating(c.rating);
            ratingBar.setIsIndicator(true);

            TextView tvName = (TextView) findViewById(R.id.tvName);
            tvName.setText(c.name);

            TextView tvLocation = (TextView) findViewById(R.id.tvLocation);
            tvLocation.setText("Lat:"+String.format("%.2f", c.location.latitude)+", Long:"+String.format("%.2f", c.location.longitude));

//            TextView tvMusic = (TextView) findViewById(R.id.tvMusic);
//            tvMusic.setText(c.music);
//
//            TextView tvMusicStyle = (TextView) findViewById(R.id.tvMusicStyle);
//            tvMusicStyle.setText(c.music_style);
//
//            TextView tvRating = (TextView) findViewById(R.id.tvRating);
//            tvRating.setText(""+c.rating);

            TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
            tvDescription.setText(c.description);


        }


        //Floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnNavigate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            String url = (String) c.url;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
            startActivity(intent);

            }
        });


        //TODO: load club info in the form
    }


    public void imgbtnBackClick(View v) {
    finish();
    }
}
