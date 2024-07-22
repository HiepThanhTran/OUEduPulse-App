package com.fh.app_student_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import me.relex.circleindicator.CircleIndicator3;

public class OnboardingActivity extends AppCompatActivity {
    private Button btnSkip;
    private ViewPager2 viewPager;
    private RelativeLayout layoutBottom;
    private CircleIndicator3 circleIndicator;
    private Button btnNext;
    private Button btnStart;
    private OnboardingViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        initOnboarding();
        viewPagerAdapter = new OnboardingViewPagerAdapter(this);
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
        layoutBottom = findViewById(R.id.layoutBottom);
        btnNext = findViewById(R.id.btnNext);
        circleIndicator = findViewById(R.id.circleIndicator);
        btnStart = findViewById(R.id.btnStart);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() < 2) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnboardingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}