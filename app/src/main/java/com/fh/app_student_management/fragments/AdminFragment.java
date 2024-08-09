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
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.ui.admin.ClassListActivity;
import com.fh.app_student_management.ui.admin.LecturerListActivity;
import com.fh.app_student_management.ui.admin.StatisticalActivity;
import com.fh.app_student_management.ui.admin.StudentListActivity;
import com.fh.app_student_management.ui.admin.SubjectListActivity;
import com.fh.app_student_management.utilities.Constants;

import java.util.Map;
import java.util.Objects;

public class AdminFragment extends Fragment {

    private User user;

    private CardView btnToUserManagement;
    private CardView btnToSubjectManagement;
    private CardView btnToClassManagement;
    private CardView btnToStudentManagement;
    private CardView btnToStatisticalAdmin;

    public static AdminFragment newInstance(Map<String, String> params) {
        AdminFragment fragment = new AdminFragment();
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
        View view = inflater.inflate(R.layout.z_fragment_admin, container, false);

        initAdminView(view);
        handleEventListener();

        return view;
    }

    private void initAdminView(View view) {
        btnToUserManagement = view.findViewById(R.id.btnToUserManagement);
        btnToSubjectManagement = view.findViewById(R.id.btnToSubjectManagement);
        btnToClassManagement = view.findViewById(R.id.btnToClassManagement);
        btnToStudentManagement = view.findViewById(R.id.btnToStudentManagement);
        btnToStatisticalAdmin = view.findViewById(R.id.btnToStatisticalAdmin);
    }

    private void handleEventListener() {
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.USER_ID, user.getId());

        btnToUserManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LecturerListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnToSubjectManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SubjectListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnToClassManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ClassListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnToStudentManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StudentListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnToStatisticalAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StatisticalActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}
