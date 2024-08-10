package com.fh.app_student_management.ui.admin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.admin.StudentRecycleViewAdapter;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.AcademicYear;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.data.entities.Student;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.data.relations.StudentWithRelations;
import com.fh.app_student_management.data.relations.SubjectWithRelations;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;
import com.fh.app_student_management.utilities.Validator;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class StudentListActivity extends AppCompatActivity {

    private AppDatabase db;
    private ArrayList<Semester> semesters;
    private ArrayList<String> semesterNames;
    private long selectedSemesterId;
    private ArrayList<SubjectWithRelations> subjects;
    private ArrayList<String> subjectNames;
    private long selectedSubjectId;
    private ArrayList<Major> majors;
    private ArrayList<String> majorNames;
    private long selectedMajorId;
    private ArrayList<Class> classes;
    private ArrayList<String> classNames;
    private long selectedClassId;
    private ArrayList<AcademicYear> academicYears;
    private ArrayList<String> academicYearNames;
    private long selectedAcademicYearId;
    private StudentRecycleViewAdapter studentRecycleViewAdapter;

    private RelativeLayout layoutStudent;
    private ImageView btnBack;
    private SearchView searchViewStudent;
    private EditText edtSemester;
    private EditText edtClass;
    private EditText edtSubject;
    private Button btnAddStudent;

    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_student);

        initAddStudentView();
        handleEventListener();
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
    }

    private void initAddStudentView() {
        layoutStudent = findViewById(R.id.layoutStudent);
        btnBack = findViewById(R.id.btnBack);
        searchViewStudent = findViewById(R.id.searchViewStudent);
        edtSemester = findViewById(R.id.edtSemester);
        edtClass = findViewById(R.id.edtClass);
        edtSubject = findViewById(R.id.edtSubject);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        bottomSheetDialog = new BottomSheetDialog(this);

        db = AppDatabase.getInstance(this);

        semesters = new ArrayList<>(db.semesterDAO().getAll());
        semesterNames = new ArrayList<>(semesters.size() + 1);
        semesterNames.add(0, "--- Chọn học kỳ ---");
        for (int i = 0; i < semesters.size(); i++) {
            String startDate = Utils.formatDate("MM/yyyy").format(semesters.get(i).getStartDate());
            String endDate = Utils.formatDate("MM/yyyy").format(semesters.get(i).getEndDate());
            String semesterName = String.format("%s (%s - %s)", semesters.get(i).getName(), startDate, endDate);
            semesterNames.add(semesterName);
        }

        majors = new ArrayList<>(db.majorDAO().getAll());
        majorNames = new ArrayList<>(majors.size() + 1);
        majorNames.add(0, "--- Chọn ngành ---");
        for (int i = 0; i < majors.size(); i++) {
            majorNames.add(majors.get(i).getName());
        }

        classes = new ArrayList<>(db.classDAO().getAll());
        classNames = new ArrayList<>(classes.size() + 1);
        classNames.add(0, "--- Chọn lớp ---");
        for (int i = 0; i < classes.size(); i++) {
            classNames.add(classes.get(i).getName());
        }

        academicYears = new ArrayList<>(db.academicYearDAO().getAll());
        academicYearNames = new ArrayList<>(academicYears.size() + 1);
        academicYearNames.add(0, "--- Chọn khóa học ---");
        for (int i = 0; i < academicYears.size(); i++) {
            academicYearNames.add(academicYears.get(i).getName());
        }

        ArrayList<StudentWithRelations> students = new ArrayList<>(db.studentDAO().getAllWithRelations());

        studentRecycleViewAdapter = new StudentRecycleViewAdapter(this, students);
        RecyclerView rvStudent = findViewById(R.id.rvStudent);
        rvStudent.setLayoutManager(new LinearLayoutManager(this));
        rvStudent.setAdapter(studentRecycleViewAdapter);
    }

    @SuppressLint("InflateParams")
    private void handleEventListener() {
        layoutStudent.setOnClickListener(v -> {
            if (v.getId() == R.id.layoutStudent) {
                searchViewStudent.clearFocus();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        searchViewStudent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                studentRecycleViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                studentRecycleViewAdapter.getFilter().filter(newText);
                return false;
            }
        });

        edtSemester.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn học kỳ")
                .setItems(semesterNames.toArray(new CharSequence[0]), (dialog, which) -> {
                    if (which == 0) {
                        edtSemester.setText("");
                        edtClass.setText("");
                        edtSubject.setText("");
                        selectedSemesterId = 0;
                        selectedClassId = 0;
                        selectedSubjectId = 0;
                        studentRecycleViewAdapter.resetFilteredList();
                        return;
                    }

                    selectedSemesterId = semesters.get(which - 1).getId();
                    edtSemester.setText(semesterNames.get(which));
                    edtClass.setText("");
                    edtSubject.setText("");
                    selectedClassId = 0;
                    selectedSubjectId = 0;

                    studentRecycleViewAdapter.setFilteredList(new ArrayList<>(db.studentDAO().getBySemester(selectedSemesterId)));
                    searchViewStudent.setQuery("", false);
                    searchViewStudent.clearFocus();
                })
                .show());

        edtClass.setOnClickListener(v -> {
            if (edtSemester.getText().toString().isEmpty()) {
                Utils.showToast(this, "Chưa chọn học kỳ");
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn lớp");
            builder.setItems(classNames.toArray(new CharSequence[0]), (dialog, which) -> {
                if (which == 0) {
                    edtClass.setText("");
                    edtSubject.setText("");
                    selectedClassId = 0;
                    selectedSubjectId = 0;
                    studentRecycleViewAdapter.setFilteredList(new ArrayList<>(db.studentDAO().getBySemester(selectedSemesterId)));
                    searchViewStudent.setQuery("", false);
                    searchViewStudent.clearFocus();
                    return;
                }

                selectedClassId = classes.get(which - 1).getId();
                edtClass.setText(classNames.get(which));
                edtSubject.setText("");
                selectedSubjectId = 0;

                studentRecycleViewAdapter.setFilteredList(new ArrayList<>(db.studentDAO().getBySemesterAndClass(selectedSemesterId, selectedClassId)));
                searchViewStudent.setQuery("", false);
                searchViewStudent.clearFocus();
            });
            builder.show();
        });

        edtSubject.setOnClickListener(v -> {
            if (edtSemester.getText().toString().isEmpty()) {
                Utils.showToast(this, "Chưa chọn học kỳ");
                return;
            }

            if (edtClass.getText().toString().isEmpty()) {
                Utils.showToast(this, "Chưa chọn lớp");
                return;
            }

            subjects = new ArrayList<>(db.subjectDAO().getByClassAndSemester(selectedClassId, selectedSemesterId));
            subjectNames = new ArrayList<>(subjects.size() + 1);
            subjectNames.add(0, "--- Chọn môn học ---");
            for (int i = 0; i < subjects.size(); i++) {
                subjectNames.add(subjects.get(i).getSubject().getName());
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Chọn môn học");
            builder.setItems(subjectNames.toArray(new CharSequence[0]), (dialog, which) -> {
                if (which == 0) {
                    edtSubject.setText("");
                    selectedSubjectId = 0;
                    studentRecycleViewAdapter.setFilteredList(new ArrayList<>(db.studentDAO().getBySemesterAndClass(selectedSemesterId, selectedClassId)));
                    searchViewStudent.setQuery("", false);
                    searchViewStudent.clearFocus();
                    return;
                }

                selectedSubjectId = subjects.get(which - 1).getSubject().getId();
                edtSubject.setText(subjectNames.get(which));

                studentRecycleViewAdapter.setFilteredList(new ArrayList<>(db.studentDAO().getBySemesterAndClassAndSubject(selectedSemesterId, selectedClassId, selectedSubjectId)));
                searchViewStudent.setQuery("", false);
                searchViewStudent.clearFocus();
            });
            builder.show();
        });

        btnAddStudent.setOnClickListener(v -> showAddStudentDialog());
    }

    @SuppressLint("InflateParams")
    private void showAddStudentDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.admin_bottom_sheet_add_student, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setSkipCollapsed(true);
        behavior.setDraggable(false);
        behavior.setHideable(true);
        bottomSheetDialog.show();

        view.findViewById(R.id.iconCamera).setOnClickListener(v -> ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        view.findViewById(R.id.edtDob).setOnClickListener(this::showDatePickerDialog);

        view.findViewById(R.id.edtMajor).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn lớp")
                .setItems(majorNames.toArray(new CharSequence[0]), (dialog, which) -> {
                    selectedMajorId = majors.get(which - 1).getId();
                    ((EditText) view.findViewById(R.id.edtMajor)).setText(majorNames.get(which));
                })
                .show());

        view.findViewById(R.id.edtClass).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn ngành")
                .setItems(classNames.toArray(new CharSequence[0]), (dialog, which) -> {
                    selectedClassId = classes.get(which - 1).getId();
                    ((EditText) view.findViewById(R.id.edtClass)).setText(classNames.get(which));
                })
                .show());

        view.findViewById(R.id.edtAcademicYear).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn năm học")
                .setItems(academicYearNames.toArray(new CharSequence[0]), (dialog, which) -> {
                    selectedAcademicYearId = academicYears.get(which - 1).getId();
                    ((EditText) view.findViewById(R.id.edtAcademicYear)).setText(academicYearNames.get(which));
                })
                .show());

        view.findViewById(R.id.btnAddStudent).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Thêm sinh viên mới?")
                .setPositiveButton("Có", (dialog, which) -> performAddStudent(view))
                .setNegativeButton("Không", null)
                .show());
    }

    private void performAddStudent(View view) {
        if (!validateInputs(view)) return;

        StudentWithRelations studentWithRelations = new StudentWithRelations();
        studentWithRelations.setUser(new User());
        studentWithRelations.setStudent(new Student());

        try {
            studentWithRelations.getUser().setAvatar(Utils.getBytesFromBitmap(Utils.getBitmapFromView(view.findViewById(R.id.avatar))));
            studentWithRelations.getUser().setEmail(((EditText) view.findViewById(R.id.edtEmail)).getText().toString());
            studentWithRelations.getUser().setPassword(Utils.hashPassword("123456"));
            studentWithRelations.getUser().setFullName(((EditText) view.findViewById(R.id.edtFullName)).getText().toString());
            studentWithRelations.getUser().setDob(Utils.formatDate("dd/MM/YYYY").parse(((EditText) view.findViewById(R.id.edtDob)).getText().toString()));
            studentWithRelations.getUser().setAddress(((EditText) view.findViewById(R.id.edtAddress)).getText().toString());
            studentWithRelations.getUser().setRole(Constants.Role.STUDENT);
            studentWithRelations.getStudent().setMajorId(selectedMajorId);
            studentWithRelations.getStudent().setClassId(selectedClassId);
            studentWithRelations.getStudent().setAcademicYearId(selectedAcademicYearId);

            int genderId = ((RadioGroup) view.findViewById(R.id.radioGroupGender)).getCheckedRadioButtonId();
            studentWithRelations.getUser().setGender(genderId == R.id.radioButtonMale ? Constants.MALE : Constants.FEMALE);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        studentRecycleViewAdapter.addStudent(studentWithRelations);
        bottomSheetDialog.dismiss();
        Utils.showToast(this, "Thêm thành công");
    }

    private void showDatePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (v, year, month, day) -> {
            String date = day + "/" + (month + 1) + "/" + year;
            ((TextView) view).setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean validateInputs(View view) {
        return validateNotEmpty(view, R.id.edtEmail, "Email không được để trống")
                && validateEmail(view, R.id.edtEmail)
                && validateNotEmpty(view, R.id.edtFullName, "Họ và tên không được để trống")
                && validateNotEmpty(view, R.id.edtDob, "Ngày sinh không được để trống")
                && validateNotEmpty(view, R.id.edtAddress, "Địa chỉ không được để trống")
                && validateNotEmpty(view, R.id.edtMajor, "Ngành không được để trống")
                && validateNotEmpty(view, R.id.edtClass, "Lớp không được để trống")
                && validateNotEmpty(view, R.id.edtAcademicYear, "Năm học không được để trống");
    }

    private boolean validateNotEmpty(@NonNull View view, int viewId, String errorMessage) {
        EditText editText = view.findViewById(viewId);
        if (editText == null || editText.getText().toString().trim().isEmpty()) {
            Utils.showToast(this, errorMessage);
            return false;
        }
        return true;
    }

    private boolean validateEmail(@NonNull View view, int viewId) {
        EditText editText = view.findViewById(viewId);
        if (editText != null && !Validator.isValidEmail(editText.getText().toString())) {
            Utils.showToast(this, "Email không hợp lệ");
            return false;
        }
        return true;
    }
}