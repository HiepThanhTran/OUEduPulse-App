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
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.data.relations.LecturerAndUser;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private User user;
    private LecturerAndUser lecturer;

    private LinearLayout layoutEditProfile;
    private ImageView avatar, iconCamera, btnBack;
    private EditText edtEmail, edtFullName, edtDob, edtAddress, edtSpecialization, edtDegree, edtCertificate;
    private RadioGroup radioGroupGender;
    private Button btnSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        long userId = bundle.getLong(Constants.USER_ID, 0);
        UserDAO userDAO = AppDatabase.getInstance(this).userDAO();

        user = userDAO.getById(userId);

        switch (user.getRole()) {
            case ADMIN:
                lecturer = null;
                break;
            case LECTURER:
                lecturer = AppDatabase.getInstance(this).lecturerDAO().getByUserId(user.getId());
                break;
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
        edtEmail = findViewById(R.id.edtEmail);
        avatar = findViewById(R.id.avatar);
        iconCamera = findViewById(R.id.iconCamera);
        btnBack = findViewById(R.id.btnBack);
        edtFullName = findViewById(R.id.edtFullName);
        edtDob = findViewById(R.id.edtDob);
        edtAddress = findViewById(R.id.edtAddress);
        edtSpecialization = findViewById(R.id.edtSpecialization);
        edtDegree = findViewById(R.id.edtDegree);
        edtCertificate = findViewById(R.id.edtCertificate);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        edtEmail.setText(user.getEmail());
        avatar.setImageBitmap(Utils.getBitmapFromBytes(user.getAvatar()));
        edtFullName.setText(user.getFullName());

        setTextOrHint(edtDob, user.getDob());
        setTextOrHint(edtAddress, user.getAddress());

        switch (user.getRole()) {
            case ADMIN:
                setNonEditableFields();
                break;
            case LECTURER:
                setTextOrHint(edtSpecialization, lecturer.getLecturer().getSpecialization());
                setTextOrHint(edtDegree, lecturer.getLecturer().getDegree());
                setTextOrHint(edtCertificate, lecturer.getLecturer().getCertificate());
                break;
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
                InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    inm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                } catch (Exception ex) {
                    // Ignore
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());

        iconCamera.setOnClickListener(v -> ImagePicker.with(EditProfileActivity.this).crop().compress(1024).maxResultSize(1080, 1080).start());

        edtDob.setOnClickListener(v -> showDatePickerDialog());

        btnSaveProfile.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo");
            builder.setMessage("Lưu thông tin?");
            builder.setPositiveButton("Có", (dialog, which) -> performEditProfile());
            builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    private void performEditProfile() {
        if (!validateInputs()) return;

        try {
            user.setAvatar(Utils.getBytesFromBitmap(Utils.getBitmapFromView(avatar)));
            user.setFullName(edtFullName.getText().toString());

            if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale) {
                user.setGender(Constants.MALE);
            } else {
                user.setGender(Constants.FEMALE);
            }

            user.setDob(Utils.formatDate("dd/MM/yyyy").parse(edtDob.getText().toString()));
            user.setAddress(edtAddress.getText().toString());

            if (Objects.requireNonNull(user.getRole()) == Constants.Role.LECTURER) {
                lecturer.getLecturer().setSpecialization(edtSpecialization.getText().toString());
                lecturer.getLecturer().setDegree(edtDegree.getText().toString());
                lecturer.getLecturer().setCertificate(edtCertificate.getText().toString());

                LecturerDAO lecturerDAO = AppDatabase.getInstance(this).lecturerDAO();
                lecturerDAO.update(lecturer.getLecturer());
            }

            UserDAO userDAO = AppDatabase.getInstance(this).userDAO();
            userDAO.update(user);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(Constants.USER_ID, user.getEmail());
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            edtDob.setText(date);
        }, year, month, day);

        datePickerDialog.show();
    }

    private boolean validateInputs() {
        if (isEmpty(edtEmail)) {
            showToast("Email không được để trống");
            return false;
        }
        if (isEmpty(edtFullName)) {
            showToast("Họ và tên không được để trống");
            return false;
        }
        if (isEmpty(edtDob)) {
            showToast("Ngày sinh không được để trống");
            return false;
        }
        if (isEmpty(edtAddress)) {
            showToast("Địa chỉ không được để trống");
            return false;
        }
        if (isEmpty(edtSpecialization)) {
            showToast("Chuyên ngành không được để trống");
            return false;
        }
        if (isEmpty(edtDegree)) {
            showToast("Bằng cấp không được để trống");
            return false;
        }
        if (isEmpty(edtCertificate)) {
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
            editText.setText(Utils.formatDate("dd/MM/yyyy").format(user.getDob()));
        }
    }

    @SuppressLint("SetTextI18n")
    private void setNonEditableFields() {
        edtSpecialization.setText("Không");
        edtDegree.setText("Không");
        edtCertificate.setText("Không");
        setFieldsNonEditable(edtSpecialization, edtDegree, edtCertificate);
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