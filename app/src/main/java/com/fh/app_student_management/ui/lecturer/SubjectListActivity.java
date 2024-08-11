package com.fh.app_student_management.ui.lecturer;

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
import com.fh.app_student_management.adapters.lecturer.SubjectListRecycleViewAdapter;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.data.relations.SubjectWithRelations;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;

import java.util.ArrayList;

public class SubjectListActivity extends AppCompatActivity {

    private SubjectListRecycleViewAdapter subjectListRecycleViewAdapter;

    private LinearLayout layoutSubject;
    private ImageView btnBack;
    private SearchView searchViewSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_activity_list_subject);

        initClassView();
        handleEventListener();
    }

    @SuppressLint("DefaultLocale")
    private void initClassView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        long userId = bundle.getLong(Constants.USER_ID, 0);
        long semesterId = bundle.getLong(Constants.SEMESTER_ID, 0);

        layoutSubject = findViewById(R.id.layoutSubject);
        btnBack = findViewById(R.id.btnBack);
        TextView txtSemesterName = findViewById(R.id.txtSemesterName);
        searchViewSubject = findViewById(R.id.searchViewSubject);
        RecyclerView rvSubject = findViewById(R.id.rvSubject);

        AppDatabase db = AppDatabase.getInstance(this);

        Semester semester = db.semesterDAO().getById(semesterId);
        int startYear = Utils.getYearFromDate(semester.getStartDate());
        String semesterName = String.format("%s - %d - %d", semester.getName(), startYear, startYear + 1);
        txtSemesterName.setText(semesterName);

        ArrayList<SubjectWithRelations> subjects = new ArrayList<>(db.subjectDAO().getByLecturerSemester(userId, semesterId));
        subjectListRecycleViewAdapter = new SubjectListRecycleViewAdapter(this, getIntent(), subjects);
        rvSubject.setLayoutManager(new LinearLayoutManager(this));
        rvSubject.setAdapter(subjectListRecycleViewAdapter);
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
                subjectListRecycleViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subjectListRecycleViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}