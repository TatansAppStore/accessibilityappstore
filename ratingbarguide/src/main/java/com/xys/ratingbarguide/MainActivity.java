package com.xys.ratingbarguide;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RatingBarView originRatingbar = (RatingBarView) findViewById(R.id.custom_ratingbar);
        originRatingbar.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                Toast.makeText(MainActivity.this, String.valueOf(RatingScore), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
