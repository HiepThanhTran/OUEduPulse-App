package com.fh.app_student_management.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.ClassRecycleViewAdapter;
import com.fh.app_student_management.adapters.entities.ClassItemRecycleView;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class ClassActivity extends AppCompatActivity {

    private long userId;
    private long semesterId;

    private LinearLayout layoutClass;
    private ImageView btnBack;
    private SearchView searchViewClass;

    private ClassRecycleViewAdapter classRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        userId = bundle.getLong(Constants.USER_ID, 0);
        semesterId = bundle.getLong(Constants.SEMESTER_ID, 0);

        initClassView();
        handleEventListener();
    }

    private void initClassView() {
        layoutClass = findViewById(R.id.layoutClass);
        btnBack = findViewById(R.id.btnBack);
        searchViewClass = findViewById(R.id.searchViewClass);
        RecyclerView rvClass = findViewById(R.id.rvClass);

        classRecycleViewAdapter = new ClassRecycleViewAdapter(this, getIntent(), getClasses());
        rvClass.setLayoutManager(new LinearLayoutManager(this));
        rvClass.setAdapter(classRecycleViewAdapter);
    }

    private void handleEventListener() {
        layoutClass.setOnClickListener(v -> {
            if (v.getId() == R.id.layoutClass) {
                searchViewClass.clearFocus();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        searchViewClass.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                classRecycleViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                classRecycleViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private ArrayList<ClassItemRecycleView> getClasses() {
        AppDatabase db = AppDatabase.getInstance(this);
        List<Class> classes = db.classDAO().getBySemesterAndLecturer(semesterId, userId);
        ArrayList<ClassItemRecycleView> classItemRecycleViews = new ArrayList<>();

        for (Class aClass : classes) {
            String className = aClass.getName();
            String facultyName = db.facultyDAO().getById(db.majorDAO().getById(aClass.getMajorId()).getFacultyId()).getName();
            String majorName = db.majorDAO().getById(aClass.getMajorId()).getName();
            String academicYearName = db.academicYearDAO().getById(aClass.getAcademicYearId()).getName();
            ClassItemRecycleView classItemRecycleView = new ClassItemRecycleView(aClass.getId(), semesterId, className, facultyName, majorName, academicYearName);

            classItemRecycleViews.add(classItemRecycleView);
        }

        return classItemRecycleViews;
    }
}