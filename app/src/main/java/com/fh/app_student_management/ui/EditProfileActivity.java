package com.fh.app_student_management.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private User user;
    private Lecturer lecturer;

    private LinearLayout layoutEditProfile;
    private ImageView avatar, iconCamera, btnBack;
    private EditText inputEmail;
    private EditText inputFullName;
    private TextView inputDob;
    private EditText inputAddress;
    private EditText inputSpecialization;
    private EditText inputDegree;
    private EditText inputCertificate;
    private RadioGroup radioGroupGender;
    private Button btnSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra(Constants.USER_EMAIL);
        UserDAO userDAO = AppDatabase.getInstance(this).userDAO();

        user = userDAO.getByEmail(userEmail);
        if (Objects.requireNonNull(user.getRole()) == Constants.Role.LECTURER) {
            lecturer = AppDatabase.getInstance(this).lecturerDAO().getByUserId(user.getId());
        } else {
            lecturer = null;
        }

        initEditProfileView();
        handleEventListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            avatar.setImageURI(uri);
        }
    }

    private void initEditProfileView() {
        layoutEditProfile = findViewById(R.id.layoutEditProfile);
        inputEmail = findViewById(R.id.inputEmail);
        avatar = findViewById(R.id.avatar);
        iconCamera = findViewById(R.id.iconCamera);
        btnBack = findViewById(R.id.btnBack);
        inputFullName = findViewById(R.id.inputFullName);
        inputDob = findViewById(R.id.inputDob);
        inputAddress = findViewById(R.id.inputAddress);
        inputSpecialization = findViewById(R.id.inputSpecialization);
        inputDegree = findViewById(R.id.inputDegree);
        inputCertificate = findViewById(R.id.inputCertificate);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        inputEmail.setText(user.getEmail());
        avatar.setImageBitmap(ImageUtils.getBitmapFromBytes(user.getAvatar()));
        inputFullName.setText(user.getFullName());

        setTextOrHint(inputDob, user.getDob());
        setTextOrHint(inputAddress, user.getAddress());

        if (Objects.requireNonNull(user.getRole()) == Constants.Role.LECTURER) {
            setTextOrHint(inputSpecialization, lecturer.getSpecialization());
            setTextOrHint(inputDegree, lecturer.getDegree());
            setTextOrHint(inputCertificate, lecturer.getCertificate());
        } else {
            setNonEditableFields();
        }

        if (user.getGender() == Constants.MALE) {
            radioGroupGender.check(R.id.radioButtonMale);
        } else {
            radioGroupGender.check(R.id.radioButtonFemale);
        }
    }

    private void handleEventListener() {
        layoutEditProfile.setOnClickListener(v -> {
            if (v.getId() == R.id.layoutEditProfile) {
                InputMethodManager inm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    inm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                } catch (Exception ex) {
                    // Ignore
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());

        iconCamera.setOnClickListener(v -> ImagePicker.with(EditProfileActivity.this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        inputDob.setOnClickListener(v -> showDatePickerDialog());

        btnSaveProfile.setOnClickListener(v -> {
            if (!validateInputs()) return;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo");
            builder.setMessage("Lưu thông tin?");
            builder.setPositiveButton("Có", (dialog, which) -> saveProfile());
            builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    private void saveProfile() {
        performEditProfile();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.USER_EMAIL, user.getEmail());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void performEditProfile() {
        try {
            user.setAvatar(ImageUtils.getBytesFromBitmap(ImageUtils.getBitmapFromView(avatar)));
            user.setFullName(inputFullName.getText().toString());
            if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale) {
                user.setGender(Constants.MALE);
            } else {
                user.setGender(Constants.FEMALE);
            }
            user.setDob(Constants.sdf.parse(inputDob.getText().toString()));
            user.setAddress(inputAddress.getText().toString());

            if (Objects.requireNonNull(user.getRole()) == Constants.Role.LECTURER) {
                lecturer.setSpecialization(inputSpecialization.getText().toString());
                lecturer.setDegree(inputDegree.getText().toString());
                lecturer.setCertificate(inputCertificate.getText().toString());

                LecturerDAO lecturerDAO = AppDatabase.getInstance(this).lecturerDAO();
                lecturerDAO.update(lecturer);
            }

            UserDAO userDAO = AppDatabase.getInstance(this).userDAO();
            userDAO.update(user);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    inputDob.setText(date);
                }, year, month, day);

        datePickerDialog.show();
    }

    private boolean validateInputs() {
        if (isEmpty(inputEmail)) {
            showToast("Email không được để trống");
            return false;
        }
        if (isEmpty(inputFullName)) {
            showToast("Họ và tên không được để trống");
            return false;
        }
        if (isEmpty(inputDob)) {
            showToast("Ngày sinh không được để trống");
            return false;
        }
        if (isEmpty(inputAddress)) {
            showToast("Địa chỉ không được để trống");
            return false;
        }
        if (isEmpty(inputSpecialization)) {
            showToast("Chuyên ngành không được để trống");
            return false;
        }
        if (isEmpty(inputDegree)) {
            showToast("Bằng cấp không được để trống");
            return false;
        }
        if (isEmpty(inputCertificate)) {
            showToast("Chứng chỉ không được để trống");
            return false;
        }

        return true;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private boolean isEmpty(TextView textView) {
        return textView.getText().toString().trim().isEmpty();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setTextOrHint(EditText editText, String value) {
        if (value == null || value.isEmpty()) {
            editText.setHint("Chưa có");
        } else {
            editText.setText(value);
        }
    }

    private void setTextOrHint(TextView editText, Object value) {
        if (value == null) {
            editText.setHint("Chưa có");
        } else {
            editText.setText(Constants.sdf.format(user.getDob()));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setNonEditableFields() {
        inputSpecialization.setText("Không");
        inputDegree.setText("Không");
        inputCertificate.setText("Không");
        setFieldsNonEditable(inputSpecialization, inputDegree, inputCertificate);
    }

    @SuppressLint("ResourceAsColor")
    private void setFieldsNonEditable(EditText... fields) {
        for (EditText field : fields) {
            field.setFocusable(false);
            field.setFocusableInTouchMode(false);
            field.setTextColor(R.color.disabled_grey);
        }
    }
}