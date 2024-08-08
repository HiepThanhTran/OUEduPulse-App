package com.fh.app_student_management.ui.lecturer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.ScoreRecycleViewAdapter;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.relations.StudentWithScores;
import com.fh.app_student_management.utilities.Constants;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    private long semesterId;
    private long subjectId;
    private String subjectName;

    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_activity_score);

        Intent intent = getIntent();
        semesterId = intent.getLongExtra(Constants.SEMESTER_ID, 0);
        subjectId = intent.getLongExtra(Constants.SUBJECT_ID, 0);
        subjectName = intent.getStringExtra("subjectName");

        initScoreView();
        handleEventListener();
    }

    private void initScoreView() {
        btnBack = findViewById(R.id.btnBack);
        RecyclerView rvScore = findViewById(R.id.lvScore);
        TextView txtSubjectName = findViewById(R.id.txtSubjectName);

        txtSubjectName.setText(subjectName);
        ScoreRecycleViewAdapter scoreRecycleViewAdapter = new ScoreRecycleViewAdapter(
                this, getIntent(), getStudents()
        );
        rvScore.setLayoutManager(new LinearLayoutManager(this));
        rvScore.setAdapter(scoreRecycleViewAdapter);
    }

    @SuppressLint("InflateParams")
    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());
    }

    private ArrayList<StudentWithScores> getStudents() {
        AppDatabase db = AppDatabase.getInstance(this);

        return (ArrayList<StudentWithScores>) db.studentDAO().getByClassAndSemester(subjectId, semesterId);
    }
}