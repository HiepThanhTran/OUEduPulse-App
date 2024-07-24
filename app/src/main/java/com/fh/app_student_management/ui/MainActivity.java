package com.fh.app_student_management.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db = AppDatabase.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}