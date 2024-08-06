package com.fh.app_student_management.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;

public class EditLecturerActivity extends AppCompatActivity {
    private ImageView btnBack;
    private EditText inputRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lecturer);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        inputRole = findViewById(R.id.inputRole);

        inputRole.setText("Giảng viên");

        inputRole.setFocusable(false);
        inputRole.setFocusableInTouchMode(false);
        inputRole.setClickable(true);

        inputRole.setOnClickListener(v -> showRoleSelectionDialog());
    }

    private void showRoleSelectionDialog() {
        final String[] roles = {"Giảng viên", "Admin"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn vai trò")
                .setItems(roles, (dialog, which) -> {
                    inputRole.setText(roles[which]);
                })
                .create()
                .show();
    }
}