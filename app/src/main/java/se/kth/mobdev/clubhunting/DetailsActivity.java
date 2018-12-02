package se.kth.mobdev.clubhunting;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import 	android.content.Intent;


public class DetailsActivity extends AppCompatActivity {

    Club c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
            TextView tvName = (TextView) findViewById(R.id.tvName);
            tvName.setText(c.name);

            TextView tvLocation = (TextView) findViewById(R.id.tvLocation);
            tvLocation.setText(""+c.location.latitude+", "+c.location.longitude);

            TextView tvMusic = (TextView) findViewById(R.id.tvMusic);
            tvMusic.setText(c.music);

            TextView tvMusicStyle = (TextView) findViewById(R.id.tvMusicStyle);
            tvMusicStyle.setText(c.music_style);

            TextView tvRating = (TextView) findViewById(R.id.tvRating);
            tvRating.setText(""+c.rating);

            TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
            tvDescription.setText(c.description);


        }


        //Floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

}
