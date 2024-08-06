package com.fh.app_student_management.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.fh.app_student_management.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StatisticalForLecturerActivity extends AppCompatActivity {
    private ImageView btnBack;
    private EditText inputSemester;
    private EditText inputSubject;
    private PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_for_lecturer);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        inputSemester = findViewById(R.id.inputSemester);
        inputSubject = findViewById(R.id.inputSubject);

        inputSemester.setText("Học kì 1");
        inputSubject.setText("Lập trình hiện đại");

        inputSemester.setOnClickListener(v -> showSemesterDialog());
        inputSubject.setOnClickListener(v -> showSubjectDialog());


        chart = findViewById(R.id.chart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(10f, "1"));
        entries.add(new PieEntry(10f, "2"));
        entries.add(new PieEntry(10f, "3"));
        entries.add(new PieEntry(10f, "4"));
        entries.add(new PieEntry(10f, "5"));
        entries.add(new PieEntry(10f, "6"));
        entries.add(new PieEntry(10f, "7"));
        entries.add(new PieEntry(10f, "8"));
        entries.add(new PieEntry(10f, "9"));
        entries.add(new PieEntry(10f, "10"));

        PieDataSet pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextSize(14f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(chart));
        pieData.setValueTextSize(14f);

        chart.setData(pieData);
        chart.getDescription().setEnabled(false);
        chart.setUsePercentValues(true);
        chart.animateY(100);
        chart.getLegend().setEnabled(false);
        chart.invalidate();
    }

    private void showSemesterDialog() {
        final String[] semesters = {"Học kì 1", "Học kì 2"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn học kì")
                .setItems(semesters, (dialog, which) -> inputSemester.setText(semesters[which]))
                .show();
    }

    private void showSubjectDialog() {
        final String[] subjects = {"Lập trình hiện đại", "Lập trình di động"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn môn học")
                .setItems(subjects, (dialog, which) -> inputSubject.setText(subjects[which]))
                .show();
    }
}