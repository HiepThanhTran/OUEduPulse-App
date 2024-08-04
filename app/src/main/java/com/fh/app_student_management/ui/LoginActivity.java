package com.fh.app_student_management.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.dao.UserDAO;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.PasswordUtils;
import com.fh.app_student_management.utilities.Validator;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout layoutLogin;
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txtRegister;
    private CheckBox chkRememberPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLoginView();
        handleEventListener();
    }

    private void initLoginView() {
        layoutLogin = findViewById(R.id.layoutLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        chkRememberPassword = findViewById(R.id.chkRememberPassword);
    }

    private void handleEventListener() {
        layoutLogin.setOnClickListener(v -> {
            if (v.getId() == R.id.layoutLogin) {
                InputMethodManager inm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    inm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                } catch (Exception ex) {
                    // Ignore
                }
            }
        });

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Validator.isValidEmail(email)) {
                Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            performLogin(email, password);
        });

        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void performLogin(String email, String password) {
        UserDAO userDao = AppDatabase.getInstance(this).userDAO();
        User user = userDao.getByEmail(email);
        if (user == null || !PasswordUtils.verifyPassword(password, user.getPassword())) {
            Toast.makeText(this, "Email hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (chkRememberPassword.isChecked()) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("userEmail", user.getEmail());

            editor.apply();
        }

        Intent intent = new Intent(LoginActivity.this, BottomNavigation.class);
        intent.putExtra("userEmail", user.getEmail());

        startActivity(intent);
        finish();
    }
}