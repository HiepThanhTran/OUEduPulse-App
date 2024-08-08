package com.fh.app_student_management.ui.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class StudentListActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnAddStudent;
    private Button btnEditStudent;
    private Button btnDeleteStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_student);

        initAddStudentView();
        handleEventListener();
    }

    private void initAddStudentView() {
        btnBack = findViewById(R.id.btnBack);
        btnAddStudent = findViewById(R.id.btnAddStudent);
//        btnEditStudent = findViewById(R.id.btnEditStudent);
//        btnDeleteStudent = findViewById(R.id.btnDeleteStudent);
    }

    @SuppressLint("InflateParams")
    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());

        btnAddStudent.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View view1 = LayoutInflater.from(this).inflate(R.layout.admin_bottom_sheet_add_student, null);
            bottomSheetDialog.setContentView(view1);

            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            assert bottomSheet != null;
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);

            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setSkipCollapsed(true);

            bottomSheetDialog.show();
        });
    }
}