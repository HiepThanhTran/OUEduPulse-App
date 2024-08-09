package com.fh.app_student_management.ui.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.admin.LecturerRecycleViewAdapter;
import com.fh.app_student_management.adapters.admin.SubjectRecycleViewAdapter;
import com.fh.app_student_management.data.AppDatabase;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SubjectListActivity extends AppCompatActivity {

    private BottomSheetDialog bottomSheetDialog;
    private SubjectRecycleViewAdapter subjectRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_subject);

        initSubjectListView();
        handleEventListener();
    }

    private void initSubjectListView() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        bottomSheetDialog = new BottomSheetDialog(this);
        AppDatabase db = AppDatabase.getInstance(this);
    }

    @SuppressLint("InflateParams")
    private void handleEventListener() {
    }
}