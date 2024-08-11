package com.fh.app_student_management.ui.admin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.admin.ClassStatisticalRecycleViewAdapter;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.AcademicYear;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Faculty;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.relations.ClassWithRelations;

import java.util.ArrayList;
import java.util.List;

public class StatisticalClassActivity extends AppCompatActivity {

    private AppDatabase db;
    private ArrayList<Faculty> faculties;
    private ArrayList<String> facultyNames;
    private long selectedFacultyId;
    private ArrayList<Major> majors;
    private ArrayList<String> majorNames;
    private long selectedMajorId;
    private ArrayList<AcademicYear> academicYears;
    private ArrayList<String> academicYearNames;
    private long selectedAcademicYearId;

    private ImageView btnBack;
    private EditText edtFaculty;
    private EditText edtMajor;
    private EditText edtAcademicYear;
    private LinearLayout titleTable;
    private TextView txtClassCount;
    private RecyclerView rvClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_statistical_class);

        initStatisticalClassView();
        handleEventListener();
    }

    private void initStatisticalClassView() {
        btnBack = findViewById(R.id.btnBack);
        edtFaculty = findViewById(R.id.edtFaculty);
        edtMajor = findViewById(R.id.edtMajor);
        edtAcademicYear = findViewById(R.id.edtAcademicYear);
        titleTable = findViewById(R.id.titleTable);
        txtClassCount = findViewById(R.id.txtClassCount);
        rvClass = findViewById(R.id.rvClass);

        titleTable.setVisibility(View.GONE);

        db = AppDatabase.getInstance(this);

        faculties = new ArrayList<>(db.facultyDAO().getAll());
        facultyNames = new ArrayList<>(faculties.size() + 1);
        facultyNames.add(0, "--- Chọn khoa ---");
        for (int i = 0; i < faculties.size(); i++) {
            facultyNames.add(faculties.get(i).getName());
        }

        academicYears = new ArrayList<>(db.academicYearDAO().getAll());
        academicYearNames = new ArrayList<>(academicYears.size() + 1);
        academicYearNames.add(0, "--- Chọn khóa ---");
        for (int i = 0; i < academicYears.size(); i++) {
            academicYearNames.add(academicYears.get(i).getName());
        }

        majors = new ArrayList<>(db.majorDAO().getAll());
        majorNames = new ArrayList<>(majors.size() + 1);
        majorNames.add(0, "--- Chọn ngành ---");
        for (int i = 0; i < majors.size(); i++) {
            majorNames.add(majors.get(i).getName());
        }
    }

    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());

        edtFaculty.setOnClickListener(v -> showSelectionDialog("Chọn khoa", facultyNames, (dialog, which) -> {
            if (which == 0) {
                selectedFacultyId = 0;
                edtFaculty.setText("");
                updateClassList();
            } else {
                selectedFacultyId = faculties.get(which - 1).getId();
                edtFaculty.setText(facultyNames.get(which));
                updateClassList();
            }
        }));

        edtMajor.setOnClickListener(v -> showSelectionDialog("Chọn môn", majorNames, (dialog, which) -> {
            if (which == 0) {
                selectedMajorId = 0;
                edtMajor.setText("");
                updateClassList();
            } else {
                selectedMajorId = majors.get(which - 1).getId();
                edtMajor.setText(majorNames.get(which));
                updateClassList();
            }
        }));

        edtAcademicYear.setOnClickListener(v -> showSelectionDialog("Chọn học kỳ", academicYearNames, (dialog, which) -> {
            if (which == 0) {
                selectedAcademicYearId = 0;
                edtAcademicYear.setText("");
                updateClassList();
            } else {
                selectedAcademicYearId = academicYears.get(which - 1).getId();
                edtAcademicYear.setText(academicYearNames.get(which));
                updateClassList();
            }
        }));
    }

    private void updateClassList() {
        List<ClassWithRelations> classes = new ArrayList<>();

        if (selectedFacultyId > 0 && selectedMajorId > 0 && selectedAcademicYearId > 0) {
            classes = db.classDAO().getByFacultyMajorAcademicYear(selectedFacultyId, selectedMajorId, selectedAcademicYearId);
        } else if (selectedFacultyId > 0 && selectedMajorId > 0) {
            classes = db.classDAO().getByFacultyMajor(selectedFacultyId, selectedMajorId);
        } else if (selectedFacultyId > 0 && selectedAcademicYearId > 0) {
            classes = db.classDAO().getByFacultyAcademicYear(selectedFacultyId, selectedAcademicYearId);
        } else if (selectedMajorId > 0 && selectedAcademicYearId > 0) {
            classes = db.classDAO().getByMajorAcademicYear(selectedMajorId, selectedAcademicYearId);
        } else if (selectedFacultyId > 0) {
            classes = db.classDAO().getByFaculty(selectedFacultyId);
        } else if (selectedMajorId > 0) {
            classes = db.classDAO().getByMajor(selectedMajorId);
        } else if (selectedAcademicYearId > 0) {
            classes = db.classDAO().getByAcademicYear(selectedAcademicYearId);
        }

        rvClass.setLayoutManager(new LinearLayoutManager(this));
        rvClass.setAdapter(new ClassStatisticalRecycleViewAdapter(this, new ArrayList<>(classes)));
        txtClassCount.setText(String.valueOf(classes.size()));
        titleTable.setVisibility(View.VISIBLE);
    }

    private void showSelectionDialog(String title, List<String> options, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(options.toArray(new CharSequence[0]), listener);
        builder.show();
    }
}