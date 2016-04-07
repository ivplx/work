package com.example.user.googlemapapi;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class WelcomePage extends AppCompatActivity {
    ImageView imageViewWelcomePage;
    FrameLayout frameLayoutWelcomePage;
    Intent intentToMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        imageViewWelcomePage = (ImageView)findViewById(R.id.imageView_p2);
        frameLayoutWelcomePage = (FrameLayout)findViewById(R.id.frameLayout_p2);
        frameLayoutWelcomePage.setBackground(ContextCompat.getDrawable(this, R.drawable.iparkingbg));
        imageViewWelcomePage.setBackground(ContextCompat.getDrawable(this,R.drawable.iparking));
        imageViewWelcomePage.setVisibility(View.INVISIBLE);
        intentToMap = new Intent(this,MapsActivity.class);
        intentToMap.putExtra("val",true);

        Animation fadeIn = new AlphaAnimation(0,1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(1500);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                imageViewWelcomePage.setVisibility(View.VISIBLE);
                startActivity(intentToMap);
                WelcomePage.this.finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageViewWelcomePage.startAnimation(fadeIn);
    }
}
