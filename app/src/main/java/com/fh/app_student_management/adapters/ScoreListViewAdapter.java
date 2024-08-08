package com.fh.app_student_management.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.relations.StudentWithScores;

import java.util.ArrayList;

public class ScoreListViewAdapter extends ArrayAdapter<StudentWithScores> {

    private Activity context;
    private int layoutId;
    private ArrayList<StudentWithScores> students;

    public ScoreListViewAdapter(Activity context, int layoutId, ArrayList<StudentWithScores> students) {
        super(context, layoutId, students);
        this.context = context;
        this.layoutId = layoutId;
        this.students = students;
    }

    @NonNull
    @Override
    @SuppressLint("ViewHolder")
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);

        TextView txtId = convertView.findViewById(R.id.txtStudentId);
        TextView txtName = convertView.findViewById(R.id.txtStudentName);
        TextView edtGK = convertView.findViewById(R.id.edtGK);
        TextView edtCK = convertView.findViewById(R.id.edtCK);
        TextView edtTB = convertView.findViewById(R.id.edtTB);
        StudentWithScores student = students.get(position);

        txtId.setText(String.valueOf(student.getStudentId()));
        txtName.setText(student.getStudentName());
        edtGK.setText(String.valueOf(student.getGkScore()));
        edtCK.setText(String.valueOf(student.getCkScore()));
        edtTB.setText(String.valueOf(student.getTbScore()));

        return convertView;
    }
}