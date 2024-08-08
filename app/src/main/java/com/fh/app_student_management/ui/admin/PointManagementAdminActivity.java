package com.fh.app_student_management.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.fh.app_student_management.R;

public class PointManagementAdminActivity extends AppCompatActivity {
    private ImageView btnBack;
    private AppCompatButton btnAddStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_scores);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnAddStudent.setOnClickListener(v -> {
            Intent intent = new Intent(PointManagementAdminActivity.this, AddStudentForAdminActivity.class);
            startActivity(intent);
        });
    }
}
