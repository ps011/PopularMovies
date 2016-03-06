package tk.prasheelsoni.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {
TextView movie_name,rating,review,release;
    ImageView movie_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        movie_name=(TextView)findViewById(R.id.movie_name);
        rating=(TextView)findViewById(R.id.vote_average);
        review=(TextView)findViewById(R.id.overview);
        movie_image=(ImageView)findViewById(R.id.movie_image);
        release=(TextView)findViewById(R.id.release_date);
        Intent i=this.getIntent();
        String movie_names=i.getStringExtra("movie");
        String vote_average = i.getStringExtra("vote_average");
        String overview = i.getStringExtra("overview");
        String poster_path = i.getStringExtra("poster_path");
        String release_date = i.getStringExtra("release_date");
        Log.v("Movie Name", movie_names);
        movie_name.setText(movie_names);
        rating.setText("Rating : "+vote_average);
        review.setText(overview);
        release.setText("Release Date : "+release_date);
        movie_image.setScaleType(ImageView.ScaleType.CENTER_CROP);


        Picasso.with(this).load(poster_path).into(movie_image);
    }
}
