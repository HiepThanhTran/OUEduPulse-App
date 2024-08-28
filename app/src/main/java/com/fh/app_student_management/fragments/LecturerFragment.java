package com.fh.app_student_management.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.fh.app_student_management.R;
import com.fh.app_student_management.ui.SemesterListActivity;
import com.fh.app_student_management.ui.lecturer.StatisticalScoreActivity;
import com.fh.app_student_management.utilities.Constants;

import java.util.Map;
import java.util.Objects;

public class LecturerFragment extends Fragment {

    private long userId;

    private CardView btnToSemester;
    private CardView btnToStatistical;

    @NonNull
    public static LecturerFragment newInstance(@NonNull Map<String, String> params) {
        LecturerFragment fragment = new LecturerFragment();
        Bundle args = new Bundle();

        args.putString(Constants.USER_ID, params.get(Constants.USER_ID));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            userId = Long.parseLong(Objects.requireNonNull(requireArguments().getString(Constants.USER_ID)));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.z_fragment_lecturer, container, false);
        Window window = requireActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.grey, requireActivity().getTheme()));

        initLecturerView(view);
        handleEventListener();

        return view;
    }

    private void initLecturerView(@NonNull View view) {
        btnToSemester = view.findViewById(R.id.btnToSemester);
        btnToStatistical = view.findViewById(R.id.btnToStatistical);
    }

    private void handleEventListener() {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.USER_ID, userId);

        btnToSemester.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SemesterListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnToStatistical.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StatisticalScoreActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}