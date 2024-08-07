package com.fh.app_student_management;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class PointManagementAdminActivity extends AppCompatActivity {
    private ImageView btnBack;
    private AppCompatButton btnAddStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_management_admin);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnAddStudent = findViewById(R.id.btnAddStudent);

    }
}