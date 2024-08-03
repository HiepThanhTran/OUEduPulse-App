package com.fh.app_student_management.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.fh.app_student_management.fragments.OnboardingFragment1;
import com.fh.app_student_management.fragments.OnboardingFragment2;
import com.fh.app_student_management.fragments.OnboardingFragment3;

public class OnboardingViewPagerAdapter extends FragmentStateAdapter {

    public OnboardingViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new OnboardingFragment2();
            case 2:
                return new OnboardingFragment3();
            default:
                return new OnboardingFragment1();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}