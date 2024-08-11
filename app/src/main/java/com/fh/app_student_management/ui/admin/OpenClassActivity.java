package com.fh.app_student_management.ui.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.LecturerSubjectCrossRef;
import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.data.entities.Subject;
import com.fh.app_student_management.data.entities.SubjectSemesterCrossRef;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.data.relations.LecturerAndUser;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;

import java.util.List;

public class OpenClassActivity extends AppCompatActivity {

    private AppDatabase db;
    private List<Subject> subjects;
    private String[] subjectNames;
    private Subject selectedSubject;
    private List<User> lecturers;
    private String[] lecturerNames;
    private LecturerAndUser selectedLecturer;
    private List<Semester> semesters;
    private String[] semesterNames;
    private Semester selectedSemester;

    private ImageView btnBack;
    private EditText edtSubject;
    private EditText edtLecturer;
    private EditText edtSemester;
    private Button btnOpenClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_open_class);

        initCreateClassView();
        handleEventListener();
    }

    private void initCreateClassView() {
        btnBack = findViewById(R.id.btnBack);
        edtSubject = findViewById(R.id.edtSubject);
        edtLecturer = findViewById(R.id.edtLecturer);
        edtSemester = findViewById(R.id.edtSemester);
        btnOpenClass = findViewById(R.id.btnOpenClass);

        db = AppDatabase.getInstance(this);

        subjects = db.subjectDAO().getAll();
        subjectNames = new String[subjects.size()];
        for (int i = 0; i < subjects.size(); i++) {
            subjectNames[i] = subjects.get(i).getName();
        }

        lecturers = db.userDAO().getByRole(Constants.Role.LECTURER);
        lecturerNames = new String[lecturers.size()];
        for (int i = 0; i < lecturers.size(); i++) {
            lecturerNames[i] = lecturers.get(i).getFullName();
        }

        semesters = db.semesterDAO().getAll();
        semesterNames = new String[semesters.size()];
        for (int i = 0; i < semesters.size(); i++) {
            String startDate = Utils.formatDate("MM/yyyy").format(semesters.get(i).getStartDate());
            String endDate = Utils.formatDate("MM/yyyy").format(semesters.get(i).getEndDate());
            String semesterName = String.format("%s (%s - %s)", semesters.get(i).getName(), startDate, endDate);
            semesterNames[i] = semesterName;
        }
    }

    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());

        edtSemester.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn học kì")
                .setItems(semesterNames, (dialog, which) -> {
                    selectedSemester = semesters.get(which);
                    edtSemester.setText(semesterNames[which]);
                })
                .show());

        edtSubject.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn môn học")
                .setItems(subjectNames, (dialog, which) -> {
                    selectedSubject = subjects.get(which);
                    edtSubject.setText(subjectNames[which]);
                })
                .show());

        edtLecturer.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn giảng viên")
                .setItems(lecturerNames, (dialog, which) -> {
                    selectedLecturer = db.lecturerDAO().getByUser(lecturers.get(which).getId());
                    edtLecturer.setText(lecturerNames[which]);
                })
                .show());

        btnOpenClass.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Mở lớp?")
                .setPositiveButton("Có", (dialog, which) -> performOpenClass())
                .setNegativeButton("Không", null)
                .show());
    }

    private void performOpenClass() {
        LecturerSubjectCrossRef lecturerSubjectCrossRef = new LecturerSubjectCrossRef();
        lecturerSubjectCrossRef.setLecturerId(selectedLecturer.getLecturer().getId());
        lecturerSubjectCrossRef.setSubjectId(selectedSubject.getId());
        db.crossRefDAO().insertLecturerSubjectCrossRef(lecturerSubjectCrossRef);

        SubjectSemesterCrossRef subjectSemesterCrossRef = new SubjectSemesterCrossRef();
        subjectSemesterCrossRef.setSubjectId(selectedSubject.getId());
        subjectSemesterCrossRef.setSemesterId(selectedSemester.getId());
        db.crossRefDAO().insertSubjectSemesterCrossRef(subjectSemesterCrossRef);

        Utils.showToast(this, "Tạo lớp thành công");
        finish();
    }
}