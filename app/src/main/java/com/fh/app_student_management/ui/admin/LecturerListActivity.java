package com.fh.app_student_management.ui.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.fh.app_student_management.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class LecturerListActivity extends AppCompatActivity {

    private ImageView btnBack;
    private AppCompatButton btnToAddLecturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_lecturer);

        initLecturerListView();
        handleEventListener();
    }

    private void initLecturerListView() {
        btnBack = findViewById(R.id.btnBack);
        btnToAddLecturer = findViewById(R.id.btnAddLecturer);
    }

    @SuppressLint("InflateParams")
    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());

        btnToAddLecturer.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View view1 = LayoutInflater.from(this).inflate(R.layout.admin_bottom_sheet_add_lecturer, null);
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