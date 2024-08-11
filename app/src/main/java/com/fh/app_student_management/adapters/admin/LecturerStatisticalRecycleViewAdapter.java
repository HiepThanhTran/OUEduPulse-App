package com.fh.app_student_management.adapters.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.relations.StatisticalOfLecturer;

import java.util.ArrayList;

public class LecturerStatisticalRecycleViewAdapter extends RecyclerView.Adapter<LecturerStatisticalRecycleViewAdapter.LecturerStatisticalViewHolder> {

    private final Context context;
    private final ArrayList<StatisticalOfLecturer> statisticalOfLecturers;

    public LecturerStatisticalRecycleViewAdapter(Context context, ArrayList<StatisticalOfLecturer> statisticalOfLecturers) {
        this.context = context;
        this.statisticalOfLecturers = statisticalOfLecturers;
    }

    @NonNull
    @Override
    public LecturerStatisticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_layout_recycle_view_statistical_lecturer, parent, false);

        return new LecturerStatisticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LecturerStatisticalViewHolder holder, int position) {
        StatisticalOfLecturer statisticalOfLecturer = statisticalOfLecturers.get(position);
        holder.txtLecturerNumber.setText(String.valueOf(position + 1));
        holder.txtSubjectName.setText(statisticalOfLecturer.getSubjectName());
        holder.txtClassName.setText(statisticalOfLecturer.getClassName());
        holder.txtSemesterName.setText(statisticalOfLecturer.getSemesterName());
    }

    @Override
    public int getItemCount() {
        return statisticalOfLecturers.size();
    }

    public static class LecturerStatisticalViewHolder extends RecyclerView.ViewHolder {

        public final TextView txtLecturerNumber;
        public final TextView txtSubjectName;
        public final TextView txtClassName;
        public final TextView txtSemesterName;

        public LecturerStatisticalViewHolder(View view) {
            super(view);

            txtLecturerNumber = view.findViewById(R.id.txtLecturerNumber);
            txtSubjectName = view.findViewById(R.id.txtSubjectName);
            txtClassName = view.findViewById(R.id.txtClassName);
            txtSemesterName = view.findViewById(R.id.txtSemesterName);
        }
    }
}
