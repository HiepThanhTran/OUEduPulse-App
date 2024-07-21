package com.fh.app_student_management;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingFragment3 extends Fragment {
    private Button btnStart;
    private View mView;

    public OnboardingFragment3() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       mView = inflater.inflate(R.layout.fragment_onboarding_3, container, false);
       btnStart = mView.findViewById(R.id.btnStart);
       btnStart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), MainActivity.class);
               getActivity().startActivity(intent);
           }
       });
       return mView;
    }
}