package com.example.user.testwelcomepageapi22;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    Button button;
    RelativeLayout relativeLayout;
    ImageView imageView;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(btnListener);
        imageView = (ImageView)findViewById(R.id.imageView);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        relativeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.iparkingbg));
        intent = new Intent(this,Main2Activity.class);
        intent.putExtra("val",true);
        Animation fadeIn = new AlphaAnimation(0,1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(1500);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(View.VISIBLE);

                startActivity(intent);
                MainActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(fadeIn);

    }

    Button.OnClickListener btnListener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            relativeLayout.setVisibility(View.GONE);
            relativeLayout.setBackground(null);

        }
    };

}
