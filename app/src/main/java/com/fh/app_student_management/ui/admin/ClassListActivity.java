package com.fh.app_student_management.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.fh.app_student_management.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ClassListActivity extends AppCompatActivity {

    private AppCompatButton btnAddClass;
    private AppCompatButton btnEditClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_class);

        initClassListView();
        handleEventListener();
    }

    private void initClassListView() {
        btnAddClass = findViewById(R.id.btnAddClass);
        btnEditClass = findViewById(R.id.btnEditClass);
    }

    private void handleEventListener() {
        btnAddClass.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ClassListActivity.this);
            View view1 = LayoutInflater.from(ClassListActivity.this).inflate(R.layout.admin_bottom_sheet_add_class, null);
            bottomSheetDialog.setContentView(view1);
            bottomSheetDialog.show();
        });

        btnEditClass.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ClassListActivity.this);
            View view1 = LayoutInflater.from(ClassListActivity.this).inflate(R.layout.admin_bottom_sheet_edit_class, null);
            bottomSheetDialog.setContentView(view1);
            bottomSheetDialog.show();
        });
    }
}