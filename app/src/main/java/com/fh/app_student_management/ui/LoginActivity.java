package com.fh.app_student_management.ui;

import android.annotation.SuppressLint;
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
import com.fh.app_student_management.utilities.Utils;
import com.fh.app_student_management.utilities.Validator;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private RelativeLayout layoutLogin;
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txtRegister;
    private CheckBox chkRememberPassword;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLoginView();

        // TODO: TEMP
        edtEmail.setText("lecturer1@gmail.com");
        edtPassword.setText("user@123");
        chkRememberPassword.setChecked(true);

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
            if (!validateInputs()) return;

            performLogin();
        });

        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void performLogin() {
        UserDAO userDao = AppDatabase.getInstance(this).userDAO();
        User user = userDao.getByEmail(edtEmail.getText().toString().trim());

        if (user == null || !Utils.verifyPassword(edtPassword.getText().toString().trim()
                , user.getPassword())) {
            Toast.makeText(this, "Email hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (chkRememberPassword.isChecked()) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putLong(Constants.USER_ID, user.getId());

            editor.apply();
        }

        Intent intent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.USER_ID, user.getId());
        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }

    private boolean validateInputs() {
        if (isEmpty(edtEmail)) {
            showToast("Email không được để trống");
            return false;
        }

        if (isEmpty(edtPassword)) {
            showToast("Mật khẩu không được để trống");
            return false;
        }

        if (!Validator.isValidEmail(edtEmail.getText().toString())) {
            showToast("Email không hợp lệ");
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