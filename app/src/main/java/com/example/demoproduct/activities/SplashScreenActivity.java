package com.example.demoproduct.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.demoproduct.R;

public class SplashScreenActivity extends AppCompatActivity {
    private boolean _active = true;
    private final int _splashTime = 3000;
    private AppCompatTextView name;
//    private AppCompatImageView imageView1,imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //code that displays the content in full screen mode
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
//        imageView1=(AppCompatImageView) findViewById(R.id.modal);
//        imageView2=(AppCompatImageView)findViewById(R.id.vijaya);
        name=(AppCompatTextView) findViewById(R.id.name);
        Animation animation3= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        name.setAnimation(animation3);
//        Animation animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_left_in);
//        imageView1.setAnimation(animation);
//        Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_right_in);
//        imageView2.setAnimation(animation1);
        runSplashThread();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _active = false;
        }
        return true;
    }

    private void runSplashThread() {

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    finish();
                }
            }
        };
        splashTread.start();

    }
}
