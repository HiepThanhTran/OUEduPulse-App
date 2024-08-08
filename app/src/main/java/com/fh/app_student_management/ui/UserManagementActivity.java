package com.fh.app_student_management.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.fh.app_student_management.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class UserManagementActivity extends AppCompatActivity {
    private ImageView btnBack;
    private AppCompatButton btnToAddLecturer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnToAddLecturer = findViewById(R.id.btnToAddLecturer);
        btnToAddLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UserManagementActivity.this);
                View view1 = LayoutInflater.from(UserManagementActivity.this).inflate(R.layout.bottom_sheet_dialog_add_lecturer, null);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
            }
        });
    }
}