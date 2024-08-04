package com.fh.app_student_management.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.fh.app_student_management.R;
import com.fh.app_student_management.ui.SemesterActivity;

public class LecturerFragment extends Fragment {

    private CardView btnToSemester;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecturer, container, false);

        initLecturerView(view);
        handleEventListener();

        return view;
    }

    private void initLecturerView(View view) {
        btnToSemester = view.findViewById(R.id.btnToSemester);
    }

    private void handleEventListener() {
        btnToSemester.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SemesterActivity.class);
            startActivity(intent);
        });
    }
}