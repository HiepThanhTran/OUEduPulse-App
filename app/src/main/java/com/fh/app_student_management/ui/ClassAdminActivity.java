package com.fh.app_student_management.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.fh.app_student_management.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ClassAdminActivity extends AppCompatActivity {
    private AppCompatButton btnAddClass;
    private AppCompatButton btnEditClass = findViewById(R.id.btnEditClass);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_admin);

        btnAddClass = findViewById(R.id.btnAddClass);
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ClassAdminActivity.this);
                View view1 = LayoutInflater.from(ClassAdminActivity.this).inflate(R.layout.bottom_sheet_dialog_add_class, null);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
            }
        });

        btnEditClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ClassAdminActivity.this);
                View view1 = LayoutInflater.from(ClassAdminActivity.this).inflate(R.layout.bottom_sheet_dialog_edit_class, null);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
            }
        });
    }
}
