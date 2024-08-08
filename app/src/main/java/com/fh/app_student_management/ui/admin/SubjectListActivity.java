package com.fh.app_student_management.ui.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SubjectListActivity extends AppCompatActivity {

    private Button btnAddSubject;
    private Button btnEditSubject;
    private Button btnDeleteSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_subject);

        initSubjectListView();
        handleEventListener();
    }

    private void initSubjectListView() {
        btnAddSubject = findViewById(R.id.btnAddSubject);
        btnEditSubject = findViewById(R.id.btnEditSubject);
        btnDeleteSubject = findViewById(R.id.btnDeleteSubject);
    }

    @SuppressLint("InflateParams")
    private void handleEventListener() {
        btnAddSubject.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View view1 = LayoutInflater.from(this).inflate(R.layout.admin_bottom_sheet_add_subject, null);
            bottomSheetDialog.setContentView(view1);
            bottomSheetDialog.show();
        });

        btnEditSubject.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View view1 = LayoutInflater.from(this).inflate(R.layout.admin_bottom_sheet_edit_subject, null);
            bottomSheetDialog.setContentView(view1);
            bottomSheetDialog.show();
        });

        btnDeleteSubject.setOnClickListener(v -> {

        });
    }
}