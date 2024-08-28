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
import com.fh.app_student_management.ui.admin.ClassListActivity;
import com.fh.app_student_management.ui.admin.FeatureActivity;
import com.fh.app_student_management.ui.admin.StudentListActivity;
import com.fh.app_student_management.ui.admin.SubjectListActivity;
import com.fh.app_student_management.ui.admin.UserListActivity;
import com.fh.app_student_management.utilities.Constants;

import java.util.Map;
import java.util.Objects;

public class AdminFragment extends Fragment {

    private long userId;

    private CardView btnToUserManagement;
    private CardView btnToStudentManagement;
    private CardView btnToScoreManagement;
    private CardView btnToClassManagement;
    private CardView btnToSubjectManagement;
    private CardView btnToFeature;

    @NonNull
    public static AdminFragment newInstance(@NonNull Map<String, String> params) {
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
            userId = Long.parseLong(Objects.requireNonNull(requireArguments().getString(Constants.USER_ID)));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.z_fragment_admin, container, false);
        Window window = requireActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.grey, requireActivity().getTheme()));

        initAdminView(view);
        handleEventListener();

        return view;
    }

    private void initAdminView(@NonNull View view) {
        btnToUserManagement = view.findViewById(R.id.btnToUserManagement);
        btnToStudentManagement = view.findViewById(R.id.btnToStudentManagement);
        btnToClassManagement = view.findViewById(R.id.btnToClassManagement);
        btnToSubjectManagement = view.findViewById(R.id.btnToSubjectManagement);
        btnToFeature = view.findViewById(R.id.btnToFeature);
    }

    private void handleEventListener() {
        btnToUserManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UserListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.USER_ID, userId);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        btnToStudentManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StudentListActivity.class);
            startActivity(intent);
        });

        btnToClassManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ClassListActivity.class);
            startActivity(intent);
        });

        btnToSubjectManagement.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SubjectListActivity.class);
            startActivity(intent);
        });

        btnToFeature.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FeatureActivity.class);
            startActivity(intent);
        });
    }
}
