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
import com.fh.app_student_management.data.dao.UserDAO;
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
            String fullName = edtFullName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Validator.isValidEmail(email)) {
                Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            performRegister(fullName, email, password);
        });

        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void performRegister(String fullName, String email, String password) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(PasswordUtils.hashPassword(password));
        user.setAvatar(ImageUtils.getBytesFromDrawable(this, R.drawable.default_avatar));
        user.setRole(Constants.Role.LECTURER);

        try {
            UserDAO userDao = AppDatabase.getInstance(this).userDAO();
            userDao.insert(user);
        } catch (SQLiteConstraintException ex) {
            Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        } catch (Exception ex) {
            Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
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
    }
}