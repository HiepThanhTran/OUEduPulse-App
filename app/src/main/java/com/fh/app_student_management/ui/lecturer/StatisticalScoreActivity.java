package com.fh.app_student_management.ui.lecturer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private ArrayList<Semester> semesters;
    private ArrayList<String> semesterNames;
    private long selectedSemesterId;
    private ArrayList<SubjectWithRelations> subjects;
    private ArrayList<String> subjectNames;
    private long userId;
    private PieData pieData;
    private PieDataSet pieDataSet;
    private ArrayList<PieEntry> entries;

    private ImageView btnBack;
    private EditText edtSemester;
    private EditText edtSubject;
    private LinearLayout titleChart;
    private TextView txtSemesterName;
    private TextView txtSubjectName;
    private PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturer_activity_statistical_score);

        initStatisticalView();
        handleEventListener();
    }

    @SuppressLint("SetTextI18n")
    private void initStatisticalView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        userId = bundle.getLong(Constants.USER_ID, 0);

        btnBack = findViewById(R.id.btnBack);
        edtSemester = findViewById(R.id.edtSemester);
        edtSubject = findViewById(R.id.edtSubject);
        titleChart = findViewById(R.id.titleChart);
        txtSemesterName = findViewById(R.id.txtSemesterName);
        txtSubjectName = findViewById(R.id.txtSubjectName);
        chart = findViewById(R.id.chart);

        titleChart.setVisibility(View.GONE);

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

        entries = new ArrayList<>();
        pieDataSet = new PieDataSet(entries, "Thống kê điểm số của sinh viên");
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

        edtSemester.setOnClickListener(v -> showSelectionDialog("Chọn học kỳ", semesterNames, (dialog, which) -> {
            if (which == 0) {
                resetSelections(edtSemester, edtSubject);
            } else {
                selectedSemesterId = semesters.get(which - 1).getId();
                edtSemester.setText(semesterNames.get(which));
                resetSelections(edtSubject);
                updateChart(new ArrayList<>());
            }
        }));

        edtSubject.setOnClickListener(v -> {
            if (edtSemester.getText().toString().isEmpty()) {
                Utils.showToast(this, "Chưa chọn học kỳ");
                return;
            }

            subjects = new ArrayList<>(db.subjectDAO().getByLecturerSemester(userId, selectedSemesterId));
            subjectNames = new ArrayList<>(subjects.size() + 1);
            subjectNames.add(0, "--- Chọn môn học ---");
            for (int i = 0; i < subjects.size(); i++) {
                subjectNames.add(subjects.get(i).getSubject().getName());
            }

            showSelectionDialog("Chọn môn học", subjectNames, (dialog, which) -> {
                if (which == 0) {
                    resetSelections(edtSubject);
                } else {
                    long subjectId = subjects.get(which - 1).getSubject().getId();
                    edtSubject.setText(subjectNames.get(which));
                    titleChart.setVisibility(View.VISIBLE);
                    txtSemesterName.setText(edtSemester.getText().toString());
                    txtSubjectName.setText(subjectNames.get(which));

                    ScoreDistribution scoreDistribution = db.scoreDAO().getScoreDistribution(subjectId, selectedSemesterId);
                    entries.clear();

                    if (scoreDistribution.getExcellent() > 0) {
                        entries.add(new PieEntry(scoreDistribution.getExcellent(), "Xuất sắc"));
                    }
                    if (scoreDistribution.getGood() > 0) {
                        entries.add(new PieEntry(scoreDistribution.getGood(), "Giỏi"));
                    }
                    if (scoreDistribution.getFair() > 0) {
                        entries.add(new PieEntry(scoreDistribution.getFair(), "Khá"));
                    }
                    if (scoreDistribution.getAverage() > 0) {
                        entries.add(new PieEntry(scoreDistribution.getAverage(), "Trung bình"));
                    }

                    updateChart(entries);
                }
            });
        });
    }

    private void showSelectionDialog(String title, List<String> options, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(options.toArray(new CharSequence[0]), listener);
        builder.show();
    }

    private void updateChart(ArrayList<PieEntry> entries) {
        pieDataSet.setValues(entries);
        pieData.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private void resetSelections(EditText edtSemester, EditText edtSubject) {
        titleChart.setVisibility(View.GONE);
        selectedSemesterId = 0;
        edtSemester.setText("");
        resetSelections(edtSubject);
    }

    private void resetSelections(EditText edtSubject) {
        titleChart.setVisibility(View.GONE);
        edtSubject.setText("");
        subjectNames = null;
    }
}