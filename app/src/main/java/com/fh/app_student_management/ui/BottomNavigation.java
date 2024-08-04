package com.fh.app_student_management.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fh.app_student_management.R;
import com.fh.app_student_management.fragments.HomeFragment;
import com.fh.app_student_management.fragments.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BottomNavigation extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");
        Map<String, String> params = new HashMap<>();
        params.put("userEmail", userEmail);

        initBottomNavigationView();
        handleEventListener(params);

        loadFragment(HomeFragment.newInstance(params));
    }

    private void initBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void handleEventListener(Map<String, String> params) {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {
                loadFragment(HomeFragment.newInstance(params));
                return true;
            } else if (itemId == R.id.navSetting) {
                loadFragment(SettingFragment.newInstance(params));
                return true;
            }

            return super.onOptionsItemSelected(item);
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}