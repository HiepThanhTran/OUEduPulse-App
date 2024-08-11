package com.fh.app_student_management.adapters.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.relations.StatisticalOfSubject;

import java.util.ArrayList;

public class SubjectStatisticalRecycleViewAdapter extends RecyclerView.Adapter<SubjectStatisticalRecycleViewAdapter.SubjectStatisticalViewHolder> {

    private final Context context;
    private final ArrayList<StatisticalOfSubject> statisticalOfSubjects;

    public SubjectStatisticalRecycleViewAdapter(Context context, ArrayList<StatisticalOfSubject> statisticalOfSubjects) {
        this.context = context;
        this.statisticalOfSubjects = statisticalOfSubjects;
    }

    @NonNull
    @Override
    public SubjectStatisticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_layout_recycle_view_statistical_subject, parent, false);

        return new SubjectStatisticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectStatisticalViewHolder holder, int position) {
        StatisticalOfSubject statisticalOfSubject = statisticalOfSubjects.get(position);
        holder.txtSubjectNumber.setText(String.valueOf(position + 1));
        holder.txtSubjectName.setText(statisticalOfSubject.getSubjectName());
        holder.txtSubjectCredits.setText(String.valueOf(statisticalOfSubject.getSubjectCredits()));
        holder.txtClassName.setText(statisticalOfSubject.getClassName());
        holder.txtNumberOfStudent.setText(String.valueOf(statisticalOfSubject.getNumberOfStudents()));
    }

    @Override
    public int getItemCount() {
        return statisticalOfSubjects.size();
    }

    public static class SubjectStatisticalViewHolder extends RecyclerView.ViewHolder {

        public final TextView txtSubjectNumber;
        public final TextView txtSubjectName;
        public final TextView txtSubjectCredits;
        public final TextView txtClassName;
        public final TextView txtNumberOfStudent;

        public SubjectStatisticalViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSubjectNumber = itemView.findViewById(R.id.txtSubjectId);
            txtSubjectName = itemView.findViewById(R.id.txtSubjectName);
            txtSubjectCredits = itemView.findViewById(R.id.txtSubjectCredits);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtNumberOfStudent = itemView.findViewById(R.id.txtNumberOfStudent);
        }
    }
}
