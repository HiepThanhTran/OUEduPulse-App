package com.fh.app_student_management.ui.admin;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;

public class OpenClassActivity extends AppCompatActivity {

    private AppDatabase db;

    private ImageView btnBack;
    private EditText edtSubject;
    private EditText edtClass;
    private EditText edtLecturer;
    private EditText edtSemester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_open_class);

        initCreateClassView();
        handleEventListener();
    }

    private void initCreateClassView() {
        db = AppDatabase.getInstance(this);
        btnBack = findViewById(R.id.btnBack);
        edtSubject = findViewById(R.id.edtSubject);
        edtClass = findViewById(R.id.edtClass);
        edtLecturer = findViewById(R.id.edtLecturer);
        edtSemester = findViewById(R.id.edtSemester);
    }

    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());

        edtSemester.setOnClickListener(v -> showSemesterDialog());

        edtSubject.setOnClickListener(v -> showSubjectDialog());

        edtClass.setOnClickListener(v -> showClassDialog());

        edtLecturer.setOnClickListener(v -> showLecturerDialog());
    }

    private void showSubjectDialog() {
        String[] subjects = {"Toán", "Văn", "Anh", "Lý", "Hóa", "Sinh"};

        new AlertDialog.Builder(this)
                .setTitle("Chọn môn học")
                .setItems(subjects, (dialog, which) -> {
                    edtSubject.setText(subjects[which]);
                })
                .show();
    }

    private void showClassDialog() {
        String[] classes = {"Lớp 10A1", "Lớp 10A2", "Lớp 11B1", "Lớp 11B2"};

        new AlertDialog.Builder(this)
                .setTitle("Chọn lớp")
                .setItems(classes, (dialog, which) -> {
                    edtClass.setText(classes[which]);
                })
                .show();
    }

    private void showLecturerDialog() {
        String[] lecturers = {"Thầy A", "Cô B", "Thầy C", "Cô D"};

        new AlertDialog.Builder(this)
                .setTitle("Chọn giảng viên")
                .setItems(lecturers, (dialog, which) -> {
                    edtLecturer.setText(lecturers[which]);
                })
                .show();
    }

    private void showSemesterDialog() {
        String[] semesters = {"Học kì 1", "Học kì 2", "Học kì 3"};

        new AlertDialog.Builder(this)
                .setTitle("Chọn học kì")
                .setItems(semesters, (dialog, which) -> {
                    edtSemester.setText(semesters[which]);
                })
                .show();
    }
}