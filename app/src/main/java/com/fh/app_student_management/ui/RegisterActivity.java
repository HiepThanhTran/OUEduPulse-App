package com.fh.app_student_management.ui;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.dao.LecturerDAO;
import com.fh.app_student_management.data.dao.UserDAO;
import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.ImageUtils;
import com.fh.app_student_management.utilities.PasswordUtils;
import com.fh.app_student_management.utilities.Validator;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout layoutRegister;
    private EditText edtFullName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initRegisterView();
        handleEventListener();
    }

    private void initRegisterView() {
        layoutRegister = findViewById(R.id.layoutRegister);
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);
    }

    private void handleEventListener() {
        layoutRegister.setOnClickListener(v -> {
            if (v.getId() == R.id.layoutRegister) {
                InputMethodManager inm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    inm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                } catch (Exception ex) {
                    // Ignore
                }
            }
        });

        btnRegister.setOnClickListener(v -> {
            if (!validateInputs()) return;

            performRegister();
        });

        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void performRegister() {
        User user = new User();
        user.setFullName(edtFullName.getText().toString().trim());
        user.setEmail(edtEmail.getText().toString().trim());
        user.setPassword(PasswordUtils.hashPassword(edtPassword.getText().toString()));
        user.setAvatar(ImageUtils.getBytesFromDrawable(this, R.drawable.default_avatar));
        user.setRole(Constants.Role.LECTURER);

        try {
            UserDAO userDAO = AppDatabase.getInstance(this).userDAO();
            Long userId = userDAO.insert(user);

            Lecturer lecturer = new Lecturer();
            lecturer.setUserId(userId);
            LecturerDAO lecturerDAO = AppDatabase.getInstance(this).lecturerDAO();
            lecturerDAO.insert(lecturer);
        } catch (SQLiteConstraintException ex) {
            showToast("Email đã tồn tại!");
            return;
        } catch (Exception ex) {
            showToast("Đăng ký thất bại!");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Đăng ký thành công. Vui lòng đăng nhập để tiếp tục.");
        builder.setPositiveButton("OK", (dialog, which) -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        builder.show();
    }

    private boolean validateInputs() {
        if (isEmpty(edtFullName)) {
            showToast("Họ và tên không được để trống");
            return false;
        }

        if (isEmpty(edtEmail)) {
            showToast("Email không được để trống");
            return false;
        }

        if (isEmpty(edtPassword)) {
            showToast("Mật khẩu không được để trống");
            return false;
        }

        if (isEmpty(edtConfirmPassword)) {
            showToast("Xác nhận mật khẩu không được để trống");
        }

        if (!Validator.isValidEmail(edtEmail.getText().toString())) {
            showToast("Email không hợp lệ");
            return false;
        }
        if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            showToast("Mật khẩu không khớp");
            return false;
        }

        if (edtPassword.getText().toString().length() < 6) {
            showToast("Mật khẩu phải có ít nhất 6 ký tự");
            return false;
        }

        return true;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}