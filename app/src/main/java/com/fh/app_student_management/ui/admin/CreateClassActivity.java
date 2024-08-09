package com.fh.app_student_management.ui.admin;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fh.app_student_management.R;

public class CreateClassActivity extends AppCompatActivity {
    private ImageView btnBack;
    private EditText inputSemester, inputSubject, inputClass, inputLecturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_class);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        inputSemester = findViewById(R.id.inputSemester);
        inputSubject = findViewById(R.id.inputSubject);
        inputClass = findViewById(R.id.inputClass);
        inputLecturer = findViewById(R.id.inputLecturer);

        inputSemester.setOnClickListener(v -> showSemesterDialog());
        inputSubject.setOnClickListener(v -> showSubjectDialog());
        inputClass.setOnClickListener(v -> showClassDialog());
        inputLecturer.setOnClickListener(v -> showLecturerDialog());
    }

    private void showSemesterDialog() {
        String[] semesters = {"Học kì 1", "Học kì 2", "Học kì 3"};

        new AlertDialog.Builder(this)
                .setTitle("Chọn học kì")
                .setItems(semesters, (dialog, which) -> {
                    inputSemester.setText(semesters[which]);
                })
                .show();
    }

    private void showSubjectDialog() {
        String[] subjects = {"Toán", "Văn", "Anh", "Lý", "Hóa", "Sinh"};

        new AlertDialog.Builder(this)
                .setTitle("Chọn môn học")
                .setItems(subjects, (dialog, which) -> {
                    inputSubject.setText(subjects[which]);
                })
                .show();
    }

    private void showClassDialog() {
        String[] classes = {"Lớp 10A1", "Lớp 10A2", "Lớp 11B1", "Lớp 11B2"};

        new AlertDialog.Builder(this)
                .setTitle("Chọn lớp")
                .setItems(classes, (dialog, which) -> {
                    inputClass.setText(classes[which]);
                })
                .show();
    }

    private void showLecturerDialog() {
        String[] lecturers = {"Thầy A", "Cô B", "Thầy C", "Cô D"};

        new AlertDialog.Builder(this)
                .setTitle("Chọn giảng viên")
                .setItems(lecturers, (dialog, which) -> {
                    inputLecturer.setText(lecturers[which]);
                })
                .show();
    }
}