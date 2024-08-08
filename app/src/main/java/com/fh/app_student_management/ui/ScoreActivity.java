package com.fh.app_student_management.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.ScoreListViewAdapter;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.relations.StudentWithScores;
import com.fh.app_student_management.utilities.Constants;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    private long semesterId;
    private long subjectId;
    private String className;

    private ImageView btnBack;
    private Button btnSave;
    private ListView lvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_activity_score);

        Intent intent = getIntent();
        semesterId = intent.getLongExtra(Constants.SEMESTER_ID, 0);
        subjectId = intent.getLongExtra(Constants.SUBJECT_ID, 0);
        className = intent.getStringExtra("className");

        initScoreView();
        handleEventListener();
    }

    private void initScoreView() {
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        lvScore = findViewById(R.id.lvScore);
        TextView txtClassName = findViewById(R.id.txtClassName);

        txtClassName.setText(className);
        ScoreListViewAdapter scoreListViewAdapter = new ScoreListViewAdapter(
                this, R.layout.lecturer_layout_list_view_scores, getStudents()
        );
        lvScore.setAdapter(scoreListViewAdapter);
    }

    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());

        lvScore.setOnItemClickListener((parent, view, position, id) -> {

        });

        btnSave.setOnClickListener(v -> {

        });
    }

    private ArrayList<StudentWithScores> getStudents() {
        AppDatabase db = AppDatabase.getInstance(this);

        return (ArrayList<StudentWithScores>) db.studentDAO().getByClassAndSemester(subjectId, semesterId);
    }
}
