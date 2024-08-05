package com.fh.app_student_management.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.dao.LecturerDAO;
import com.fh.app_student_management.data.dao.UserDAO;
import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.ui.SemesterActivity;
import com.fh.app_student_management.utilities.Constants;

import java.util.Map;
import java.util.Objects;

public class LecturerFragment extends Fragment {

    private User user;

    private CardView btnToSemester;

    public static LecturerFragment newInstance(Map<String, String> params) {
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
            long userId = Long.parseLong(Objects.requireNonNull(requireArguments().getString(Constants.USER_ID)));
            AppDatabase db = AppDatabase.getInstance(requireContext());

            user = db.userDAO().getById(userId);
        }
    }

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
            intent.putExtra(Constants.USER_ID, user.getId());
            startActivity(intent);
        });
    }
}