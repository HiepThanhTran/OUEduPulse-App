package com.fh.app_student_management.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.data.relations.LecturerAndUser;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private LecturerAndUser lecturer;
    private User user;

    private LinearLayout layoutEditProfile;
    private ImageView btnBack;
    private ImageView iconCamera;
    private ImageView avatar;
    private EditText edtFullName;
    private EditText edtDob;
    private EditText edtAddress;
    private EditText edtSpecialization;
    private EditText edtDegree;
    private EditText edtCertificate;
    private RadioGroup radioGroupGender;
    private Button btnSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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
        btnBack = findViewById(R.id.btnBack);
        iconCamera = findViewById(R.id.iconCamera);
        EditText edtEmail = findViewById(R.id.edtEmail);
        avatar = findViewById(R.id.avatar);
        edtFullName = findViewById(R.id.edtFullName);
        edtDob = findViewById(R.id.edtDob);
        edtAddress = findViewById(R.id.edtAddress);
        edtSpecialization = findViewById(R.id.edtSpecialization);
        edtDegree = findViewById(R.id.edtDegree);
        edtCertificate = findViewById(R.id.edtCertificate);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        AppDatabase db = AppDatabase.getInstance(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert bundle != null;
        long userId = bundle.getLong(Constants.USER_ID, 0);
        user = db.userDAO().getById(userId);

        avatar.setImageBitmap(Utils.getBitmapFromBytes(user.getAvatar()));
        edtEmail.setText(user.getEmail());
        edtFullName.setText(user.getFullName());
        setTextOrHint(edtDob, user.getDob());
        setTextOrHint(edtAddress, user.getAddress());
        switch (user.getRole()) {
            case ADMIN:
                lecturer = null;
                setNonEditableFields();
                break;
            case LECTURER:
                lecturer = db.lecturerDAO().getByUser(user.getId());
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

        iconCamera.setOnClickListener(v -> ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        edtDob.setOnClickListener(this::showDatePickerDialog);

        btnSaveProfile.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Xác nhận thông tin?")
                .setPositiveButton("Có", (dialog, which) -> performEditProfile())
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show());
    }

    private void performEditProfile() {
        if (!validateInputs()) return;

        AppDatabase db = AppDatabase.getInstance(this);

        try {
            user.setAvatar(Utils.getBytesFromBitmap(Utils.getBitmapFromView(avatar)));
            user.setFullName(edtFullName.getText().toString());
            user.setDob(Utils.formatDate("dd/MM/yyyy").parse(edtDob.getText().toString()));
            user.setAddress(edtAddress.getText().toString());
            switch (user.getRole()) {
                case ADMIN:
                    break;
                case LECTURER:
                    lecturer.getLecturer().setSpecialization(edtSpecialization.getText().toString());
                    lecturer.getLecturer().setDegree(edtDegree.getText().toString());
                    lecturer.getLecturer().setCertificate(edtCertificate.getText().toString());

                    db.lecturerDAO().update(lecturer.getLecturer());
                    break;
            }
            if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale) {
                user.setGender(Constants.MALE);
            } else {
                user.setGender(Constants.FEMALE);
            }

            db.userDAO().update(user);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(Constants.USER_ID, user.getEmail());
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void showDatePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (v, year, month, day) -> {
            String date = day + "/" + (month + 1) + "/" + year;
            ((TextView) view).setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean validateInputs() {
        return validateNotEmpty(R.id.edtEmail, "Email không được để trống")
                && validateNotEmpty(R.id.edtFullName, "Họ và tên không được để trống")
                && validateNotEmpty(R.id.edtDob, "Ngày sinh không được để trống")
                && validateNotEmpty(R.id.edtAddress, "Địa chỉ không được để trống")
                && validateNotEmpty(R.id.edtSpecialization, "Chuyên ngành không được để trống")
                && validateNotEmpty(R.id.edtDegree, "Bằng cấp không được để trống")
                && validateNotEmpty(R.id.edtCertificate, "Chứng chỉ không được để trống");
    }

    private boolean validateNotEmpty(int viewId, String errorMessage) {
        EditText editText = findViewById(viewId);
        if (editText == null || editText.getText().toString().trim().isEmpty()) {
            Utils.showToast(this, errorMessage);
            return false;
        }
        return true;
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