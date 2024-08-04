package com.fh.app_student_management.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.utilities.Constants;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppDatabase.insertAdmin(this);

        runnable = () -> {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
            boolean isOnboarding = sharedPreferences.getBoolean("isOnboarding", false);
            String userEmail = sharedPreferences.getString(Constants.USER_EMAIL, null);

            Class<?> targetActivity = LoginActivity.class;

            targetActivity = !isOnboarding ? OnboardingActivity.class : targetActivity;
            targetActivity = userEmail != null ? BottomNavigation.class : targetActivity;

            Intent intent = new Intent(SplashActivity.this, targetActivity);

            if (userEmail != null) {
                intent.putExtra(Constants.USER_EMAIL, userEmail);
            }

            startActivity(intent);
            finish();
        };

        handler.postDelayed(runnable, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}