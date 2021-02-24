package com.kc.notesmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    ImageView imageView;
    TextView text;
    Animation topp,bottommm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView=findViewById(R.id.splashimage);
        text=findViewById(R.id.splashtext);

        topp= AnimationUtils.loadAnimation(this,R.anim.top);
        bottommm=AnimationUtils.loadAnimation(this,R.anim.bottom);

        imageView.setAnimation(topp);
        text.setAnimation(bottommm);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },2500);
    }
}