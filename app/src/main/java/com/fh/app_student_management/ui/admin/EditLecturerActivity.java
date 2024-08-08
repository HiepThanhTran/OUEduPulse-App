package com.fh.app_student_management.ui.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;

public class EditLecturerActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText inputRole;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_edit_lecturer);

        initEditLecturerView();
        handleEventListener();
    }

    @SuppressLint("SetTextI18n")
    private void initEditLecturerView() {
        btnBack = findViewById(R.id.btnBack);
        inputRole = findViewById(R.id.inputRole);

        inputRole.setText("Giảng viên");
        inputRole.setFocusable(false);
        inputRole.setFocusableInTouchMode(false);
        inputRole.setClickable(true);
    }

    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());

        inputRole.setOnClickListener(v -> showRoleSelectionDialog());
    }

    private void showRoleSelectionDialog() {
        final String[] roles = {"Giảng viên", "Admin"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn vai trò")
                .setItems(roles, (dialog, which) -> inputRole.setText(roles[which]))
                .create()
                .show();
    }
}