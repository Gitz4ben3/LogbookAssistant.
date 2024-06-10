package com.example.logbookassistant;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private static final int SPLASH_DURATION = 5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo);
        TextView logoTitle = findViewById(R.id.logoTitle);

        Animation dropDownAnimation = AnimationUtils.loadAnimation(this, R.anim.drop_down);
        Animation goUpAnimation = AnimationUtils.loadAnimation(this, R.anim.go_up);

        logo.startAnimation(dropDownAnimation);
        logoTitle.startAnimation(goUpAnimation);

        // Delay opening the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        }, SPLASH_DURATION);
    }
}
