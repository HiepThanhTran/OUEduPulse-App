package com.fh.app_student_management.ui.admin;

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
import com.fh.app_student_management.adapters.admin.LecturerStatisticalRecycleViewAdapter;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.relations.LecturerAndUser;
import com.fh.app_student_management.data.relations.StatisticalOfLecturer;

import java.util.ArrayList;
import java.util.List;

public class StatisticalLecturerActivity extends AppCompatActivity {

    private AppDatabase db;
    private ArrayList<LecturerAndUser> lecturers;
    private ArrayList<String> lecturerNames;
    private long selectedLecturerId;
    private String selectedLecturerName;

    private ImageView btnBack;
    private EditText edtLecturer;
    private LinearLayout titleTable;
    private TextView txtLecturerName;
    private RecyclerView rvLecturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_statistical_lecturer);

        initStatisticalLecturerView();
        handleEventListener();
    }

    private void initStatisticalLecturerView() {
        btnBack = findViewById(R.id.btnBack);
        edtLecturer = findViewById(R.id.edtLecturer);
        titleTable = findViewById(R.id.titleTable);
        txtLecturerName = findViewById(R.id.txtLecturerName);
        rvLecturer = findViewById(R.id.rvLecturer);

        titleTable.setVisibility(View.GONE);

        db = AppDatabase.getInstance(this);
        lecturers = new ArrayList<>(db.lecturerDAO().getAllLecturerAndUser());
        lecturerNames = new ArrayList<>(lecturers.size() + 1);
        lecturerNames.add(0, "--- Chọn giảng viên ---");
        for (int i = 0; i < lecturers.size(); i++) {
            lecturerNames.add(lecturers.get(i).getUser().getFullName());
        }
    }

    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());

        edtLecturer.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn giảng viên")
                .setItems(lecturerNames.toArray(new CharSequence[0]), (dialog, which) -> {
                    if (which == 0) {
                        selectedLecturerId = 0;
                        selectedLecturerName = null;
                        edtLecturer.setText("");
                        updateStatistical();
                    } else {
                        selectedLecturerId = lecturers.get(which - 1).getLecturer().getId();
                        selectedLecturerName = lecturers.get(which - 1).getUser().getFullName();
                        edtLecturer.setText(lecturerNames.get(which));
                        updateStatistical();
                    }
                })
                .show());
    }

    private void updateStatistical() {
        List<StatisticalOfLecturer> statisticalOfLecturers = new ArrayList<>();
        if (selectedLecturerId > 0) {
            statisticalOfLecturers = db.statisticalDAO().getStatisticalOfLecturer(selectedLecturerId);
        }
        rvLecturer.setLayoutManager(new LinearLayoutManager(this));
        rvLecturer.setAdapter(new LecturerStatisticalRecycleViewAdapter(this, new ArrayList<>(statisticalOfLecturers)));
        txtLecturerName.setText(selectedLecturerName);
        titleTable.setVisibility(View.VISIBLE);
    }
}