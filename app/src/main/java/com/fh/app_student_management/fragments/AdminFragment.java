package com.fh.app_student_management.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.fh.app_student_management.R;
import com.fh.app_student_management.ui.admin.ClassListActivity;
import com.fh.app_student_management.ui.admin.OpenClassActivity;
import com.fh.app_student_management.ui.admin.LecturerListActivity;
import com.fh.app_student_management.ui.admin.StatisticalActivity;
import com.fh.app_student_management.ui.admin.StudentListActivity;
import com.fh.app_student_management.ui.admin.SubjectListActivity;

public class AdminFragment extends Fragment {

    private CardView btnToUserManagement;
    private CardView btnToSubjectManagement;
    private CardView btnToClassManagement;
    private CardView btnToStudentManagement;
    private CardView btnToOpenClass;
    private CardView btnToStatisticalAdmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.z_fragment_admin, container, false);
        Window window = requireActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.grey, requireActivity().getTheme()));

        initAdminView(view);
        handleEventListener();

        return view;
    }

    private void initAdminView(View view) {
        btnToUserManagement = view.findViewById(R.id.btnToUserManagement);
        btnToSubjectManagement = view.findViewById(R.id.btnToSubjectManagement);
        btnToClassManagement = view.findViewById(R.id.btnToClassManagement);
        btnToStudentManagement = view.findViewById(R.id.btnToStudentManagement);
        btnToOpenClass = view.findViewById(R.id.btnToOpenClass);
        btnToStatisticalAdmin = view.findViewById(R.id.btnToStatisticalAdmin);
    }

    private void handleEventListener() {
        btnToUserManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LecturerListActivity.class);
            startActivity(intent);
        });

        btnToSubjectManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SubjectListActivity.class);
            startActivity(intent);
        });

        btnToClassManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ClassListActivity.class);
            startActivity(intent);
        });

        btnToStudentManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StudentListActivity.class);
            startActivity(intent);
        });

        btnToOpenClass.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OpenClassActivity.class);
            startActivity(intent);
        });

        btnToStatisticalAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StatisticalActivity.class);
            startActivity(intent);
        });
    }
}
