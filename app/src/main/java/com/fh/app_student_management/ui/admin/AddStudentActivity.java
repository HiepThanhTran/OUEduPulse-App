package com.fh.app_student_management.ui.admin;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;

public class AddStudentActivity extends AppCompatActivity {

    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_student);

        initAddStudentView();
        handleEventListener();
    }

    private void initAddStudentView() {
        btnBack = findViewById(R.id.btnBack);
    }

    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());
    }
}