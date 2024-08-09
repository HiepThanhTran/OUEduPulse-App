package com.fh.app_student_management.ui.admin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.admin.LecturerRecycleViewAdapter;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.data.relations.LecturerAndUser;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;
import com.fh.app_student_management.utilities.Validator;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class LecturerListActivity extends AppCompatActivity {

    private SearchView searchViewLecturer;
    private BottomSheetDialog bottomSheetDialog;
    private LecturerRecycleViewAdapter lecturerRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_lecturer);

        initLecturerListView();
        handleEventListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            ImageView avatar = bottomSheetDialog.findViewById(R.id.avatar);
            if (avatar != null) {
                avatar.setImageURI(uri);
            }
        }
        lecturerRecycleViewAdapter.onActivityResult(requestCode, resultCode, data);
    }

    private void initLecturerListView() {
        AppDatabase db = AppDatabase.getInstance(this);
        searchViewLecturer = findViewById(R.id.searchViewLecturer);
        bottomSheetDialog = new BottomSheetDialog(this);

        RecyclerView rvLecturer = findViewById(R.id.rvLecturer);
        ArrayList<LecturerAndUser> lectures = new ArrayList<>(db.lecturerDAO().getAllLecturerAndUser());
        lecturerRecycleViewAdapter = new LecturerRecycleViewAdapter(this, lectures);
        rvLecturer.setLayoutManager(new LinearLayoutManager(this));
        rvLecturer.setAdapter(lecturerRecycleViewAdapter);
    }

    private void handleEventListeners() {
        findViewById(R.id.layoutLecturer).setOnClickListener(v -> {
            if (v.getId() == R.id.layoutLecturer) {
                searchViewLecturer.clearFocus();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnAddLecturer).setOnClickListener(v -> showAddLecturerDialog());

        searchViewLecturer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                lecturerRecycleViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                lecturerRecycleViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @SuppressLint("InflateParams")
    private void showAddLecturerDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.admin_bottom_sheet_add_lecturer, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setSkipCollapsed(true);
        behavior.setDraggable(false);
        bottomSheetDialog.show();

        view.findViewById(R.id.iconCamera).setOnClickListener(v -> ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        view.findViewById(R.id.edtDob).setOnClickListener(this::showDatePickerDialog);

        view.findViewById(R.id.btnAddLecturer).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Thêm giảng viên mới?")
                .setPositiveButton("Có", (dialog, which) -> performAddLecturer(view))
                .setNegativeButton("Không", null)
                .show());
    }

    private void showDatePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (v, year, month, day) -> {
            String date = day + "/" + (month + 1) + "/" + year;
            ((TextView) view).setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void performAddLecturer(View view) {
        if (!validateInputs(view)) return;

        LecturerAndUser lecturerAndUser = new LecturerAndUser();
        lecturerAndUser.setUser(new User());
        lecturerAndUser.setLecturer(new Lecturer());

        try {
            lecturerAndUser.getUser().setAvatar(Utils.getBytesFromBitmap(Utils.getBitmapFromView(view.findViewById(R.id.avatar))));
            lecturerAndUser.getUser().setEmail(((EditText) view.findViewById(R.id.edtEmail)).getText().toString());
            lecturerAndUser.getUser().setFullName(((EditText) view.findViewById(R.id.edtFullName)).getText().toString());
            lecturerAndUser.getUser().setDob(Utils.formatDate("dd/MM/YYYY").parse(((EditText) view.findViewById(R.id.edtDob)).getText().toString()));
            lecturerAndUser.getUser().setAddress(((EditText) view.findViewById(R.id.edtAddress)).getText().toString());
            lecturerAndUser.getUser().setRole(Constants.Role.LECTURER);
            lecturerAndUser.getLecturer().setSpecialization(((EditText) view.findViewById(R.id.edtSpecialization)).getText().toString());
            lecturerAndUser.getLecturer().setDegree(((EditText) view.findViewById(R.id.edtDegree)).getText().toString());
            lecturerAndUser.getLecturer().setCertificate(((EditText) view.findViewById(R.id.edtCertificate)).getText().toString());

            int genderId = ((RadioGroup) view.findViewById(R.id.radioGroupGender)).getCheckedRadioButtonId();
            lecturerAndUser.getUser().setGender(genderId == R.id.radioButtonMale ? Constants.MALE : Constants.FEMALE);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        lecturerRecycleViewAdapter.addLecturer(lecturerAndUser);
        bottomSheetDialog.dismiss();
        Utils.showToast(this, "Thêm thành công");
    }

    private boolean validateInputs(View view) {
        return validateNotEmpty(view, R.id.edtEmail, "Email không được để trống")
                && validateNotEmpty(view, R.id.edtFullName, "Họ và tên không được để trống")
                && validateNotEmpty(view, R.id.edtDob, "Ngày sinh không được để trống")
                && validateNotEmpty(view, R.id.edtAddress, "Địa chỉ không được để trống")
                && validateNotEmpty(view, R.id.edtSpecialization, "Chuyên ngành không được để trống")
                && validateNotEmpty(view, R.id.edtDegree, "Bằng cấp không được để trống")
                && validateNotEmpty(view, R.id.edtCertificate, "Chứng chỉ không được để trống")
                && validateEmail(view, R.id.edtEmail);
    }

    private boolean validateNotEmpty(View view, int viewId, String errorMessage) {
        EditText editText = view.findViewById(viewId);
        if (editText == null || editText.getText().toString().trim().isEmpty()) {
            Utils.showToast(this, errorMessage);
            return false;
        }
        return true;
    }

    private boolean validateEmail(View view, int viewId) {
        EditText editText = view.findViewById(viewId);
        if (editText != null && !Validator.isValidEmail(editText.getText().toString())) {
            Utils.showToast(this, "Email không hợp lệ");
            return false;
        }
        return true;
    }
}