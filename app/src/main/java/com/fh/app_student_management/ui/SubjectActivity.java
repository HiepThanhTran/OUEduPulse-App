package com.fh.app_student_management.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.SubjectRecycleViewAdapter;
import com.fh.app_student_management.adapters.entities.SubjectItemRecycleView;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.data.entities.Subject;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    private AppDatabase db;

    private long userId;
    private long classId;
    private long semesterId;

    private LinearLayout layoutSubject;
    private ImageView btnBack;
    private SearchView searchViewSubject;

    private SubjectRecycleViewAdapter subjectRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        db = AppDatabase.getInstance(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        userId = bundle.getLong(Constants.USER_ID, 0);
        classId = bundle.getLong(Constants.CLASS_ID, 0);
        semesterId = bundle.getLong(Constants.SEMESTER_ID, 0);

        initClassView();
        handleEventListener();
    }

    private void initClassView() {
        layoutSubject = findViewById(R.id.layoutSubject);
        btnBack = findViewById(R.id.btnBack);
        TextView txtSemesterName = findViewById(R.id.txtSemesterName);
        searchViewSubject = findViewById(R.id.searchViewSubject);
        RecyclerView rvSubject = findViewById(R.id.rvSubject);

        Semester semester = db.semesterDAO().getById(semesterId);
        int startYear = Utils.getYearFromDate(semester.getStartDate());
        @SuppressLint("DefaultLocale") String semesterName = String.format("%s - %d - %d", semester.getName(), startYear, startYear + 1);
        txtSemesterName.setText(semesterName);

        subjectRecycleViewAdapter = new SubjectRecycleViewAdapter(this, getIntent(), getSubjects());
        rvSubject.setLayoutManager(new LinearLayoutManager(this));
        rvSubject.setAdapter(subjectRecycleViewAdapter);
    }

    private void handleEventListener() {
        layoutSubject.setOnClickListener(v -> {
            if (v.getId() == R.id.layoutSubject) {
                searchViewSubject.clearFocus();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        searchViewSubject.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                subjectRecycleViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subjectRecycleViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private ArrayList<SubjectItemRecycleView> getSubjects() {
        List<Subject> subjects = db.subjectDAO().getByClassAndLecturer(classId, userId);
        ArrayList<SubjectItemRecycleView> subjectRecycleViewAdapters = new ArrayList<>();
        for (Subject subject : subjects) {
            SubjectItemRecycleView subjectItemRecycleView = new SubjectItemRecycleView(subject.getName(), subject.getCredits());
            subjectRecycleViewAdapters.add(subjectItemRecycleView);
        }

        return subjectRecycleViewAdapters;
    }
}