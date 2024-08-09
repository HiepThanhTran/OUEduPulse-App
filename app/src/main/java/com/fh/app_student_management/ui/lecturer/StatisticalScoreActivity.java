package com.fh.app_student_management.ui.lecturer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.data.relations.ScoreDistribution;
import com.fh.app_student_management.data.relations.SubjectWithRelations;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class StatisticalScoreActivity extends AppCompatActivity {

    private AppDatabase db;

    private long userId;
    private long semesterId;

    private ImageView btnBack;
    private EditText inputSemester;
    private EditText inputSubject;
    private TextView txtSemesterName;
    private TextView txtSubjectName;

    private PieChart chart;
    private PieData pieData;
    private PieDataSet pieDataSet;
    private ArrayList<PieEntry> entries;
    private ScoreDistribution scoreDistribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_activity_statistical_score);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        userId = bundle.getLong(Constants.USER_ID, 0);

        initStatisticalView();
        handleEventListener();
    }

    @SuppressLint("SetTextI18n")
    private void initStatisticalView() {
        db = AppDatabase.getInstance(this);

        btnBack = findViewById(R.id.btnBack);
        inputSemester = findViewById(R.id.inputSemester);
        inputSubject = findViewById(R.id.inputSubject);
        txtSemesterName = findViewById(R.id.txtSemesterName);
        txtSubjectName = findViewById(R.id.txtSubjectName);
        chart = findViewById(R.id.chart);

        entries = new ArrayList<>();

        pieDataSet = new PieDataSet(entries, "Student Grades Distribution");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextSize(14f);

        pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(chart));
        pieData.setValueTextSize(14f);

        chart.setData(pieData);
        chart.getDescription().setEnabled(false);
        chart.setUsePercentValues(true);
        chart.animateY(100);
        chart.getLegend().setEnabled(false);
        chart.invalidate();
    }

    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());

        inputSemester.setOnClickListener(v -> showSemesterDialog());

        inputSubject.setOnClickListener(v -> {
            if (inputSemester.getText().toString().isEmpty()) {
                return;
            }

            showSubjectDialog();
        });
    }

    @SuppressLint("SetTextI18n")
    private void showSemesterDialog() {
        List<Semester> semesters = db.semesterDAO().getAll();
        String[] semestersName = new String[semesters.size()];
        for (int i = 0; i < semesters.size(); i++) {
            String startDate = Utils.formatDate("MM/yyyy").format(semesters.get(i).getStartDate());
            String endDate = Utils.formatDate("MM/yyyy").format(semesters.get(i).getEndDate());
            String semesterName = String.format("%s (%s - %s)", semesters.get(i).getName(), startDate, endDate);
            semestersName[i] = semesterName;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn học kì");
        builder.setItems(semestersName, (dialog, which) -> {
            semesterId = semesters.get(which).getId();
            inputSemester.setText(semestersName[which]);
            inputSubject.setText("");
            txtSemesterName.setText("");
            txtSubjectName.setText("");

            pieDataSet.setValues(new ArrayList<>());
            pieData.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        });
        builder.show();
    }

    private void showSubjectDialog() {
        List<SubjectWithRelations> subjects = db.subjectDAO().getByLecturerAndSemester(userId, semesterId);
        String[] subjectsName = new String[subjects.size()];
        for (int i = 0; i < subjects.size(); i++) {
            subjectsName[i] = subjects.get(i).getSubject().getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn môn học");
        builder.setItems(subjectsName, (dialog, which) -> {
            txtSemesterName.setText(inputSemester.getText().toString());
            txtSubjectName.setText(subjectsName[which]);
            inputSubject.setText(subjectsName[which]);

            long subjectId = subjects.get(which).getSubject().getId();
            scoreDistribution = db.scoreDAO().getScoreDistribution(subjectId, semesterId);
            entries.clear();

            if (scoreDistribution.getExcellent() > 0) {
                entries.add(new PieEntry(scoreDistribution.getExcellent(), "Xuất sắc"));
            }
            if (scoreDistribution.getGood() > 0) {
                entries.add(new PieEntry(scoreDistribution.getGood(), "Giỏi"));
            }
            if (scoreDistribution.getAverage() > 0) {
                entries.add(new PieEntry(scoreDistribution.getAverage(), "Khá"));
            }
            if (scoreDistribution.getPoor() > 0) {
                entries.add(new PieEntry(scoreDistribution.getPoor(), "Trung bình"));
            }

            pieDataSet.setValues(entries);
            pieData.notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        });
        builder.show();
    }
}