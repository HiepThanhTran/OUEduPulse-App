package com.fh.app_student_management.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.OnboardingViewPagerAdapter;
import com.fh.app_student_management.utilities.Constants;

import me.relex.circleindicator.CircleIndicator3;

public class OnboardingActivity extends AppCompatActivity {

    private CircleIndicator3 circleIndicator;
    private ViewPager2 viewPager;
    private Button btnSkip;
    private Button btnNext;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        initOnboarding();
        OnboardingViewPagerAdapter viewPagerAdapter = new OnboardingViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        circleIndicator.setViewPager(viewPager);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 2) {
                    btnSkip.setVisibility(View.INVISIBLE);
                    btnNext.setVisibility(View.GONE);
                    btnStart.setVisibility(View.VISIBLE);
                } else {
                    btnSkip.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                    btnStart.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initOnboarding() {
        btnSkip = findViewById(R.id.btnSkip);
        viewPager = findViewById(R.id.viewPager);
        btnNext = findViewById(R.id.btnNext);
        circleIndicator = findViewById(R.id.circleIndicator);
        btnStart = findViewById(R.id.btnStart);

        btnSkip.setOnClickListener(v -> viewPager.setCurrentItem(2));

        btnNext.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() < 2) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        btnStart.setOnClickListener(v -> {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isOnboarding", true);
            editor.apply();

            Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}