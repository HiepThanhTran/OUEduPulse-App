package com.fh.app_student_management.ui.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ClassListActivity extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnAddClass;
    private Button btnEditClass;
    private Button btnDeleteClass;
    private RecyclerView rvClass;
    private SearchView searchViewClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_class);

        initClassListView();
        handleEventListener();
    }

    private void initClassListView() {
        btnBack = findViewById(R.id.btnBack);
        btnAddClass = findViewById(R.id.btnAddClass);
        btnEditClass = findViewById(R.id.btnEditClass);
        btnDeleteClass = findViewById(R.id.btnDeleteClass);
        rvClass = findViewById(R.id.rvClass);
        searchViewClass = findViewById(R.id.searchViewClass);
    }

    @SuppressLint("InflateParams")
    private void handleEventListener() {
        btnBack.setOnClickListener(v -> finish());

        btnAddClass.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View view1 = LayoutInflater.from(this).inflate(R.layout.admin_bottom_sheet_add_class, null);
            bottomSheetDialog.setContentView(view1);
            bottomSheetDialog.show();
        });

        btnEditClass.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            View view1 = LayoutInflater.from(this).inflate(R.layout.admin_bottom_sheet_edit_class, null);
            bottomSheetDialog.setContentView(view1);
            bottomSheetDialog.show();
        });

        btnDeleteClass.setOnClickListener(v -> {

        });
    }
}