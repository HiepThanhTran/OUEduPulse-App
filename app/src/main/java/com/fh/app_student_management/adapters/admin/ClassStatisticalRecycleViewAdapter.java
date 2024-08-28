package com.fh.app_student_management.adapters.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.relations.ClassWithRelations;

import java.util.ArrayList;

public class ClassStatisticalRecycleViewAdapter extends RecyclerView.Adapter<ClassStatisticalRecycleViewAdapter.ClassStatisticalViewHolder> {

    private final Context context;
    private final ArrayList<ClassWithRelations> classes;

    public ClassStatisticalRecycleViewAdapter(Context context, ArrayList<ClassWithRelations> classes) {
        this.context = context;
        this.classes = classes;
    }

    @NonNull
    @Override
    public ClassStatisticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_layout_recycle_view_statistical_class, parent, false);

        return new ClassStatisticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassStatisticalViewHolder holder, int position) {
        ClassWithRelations aClass = classes.get(position);
        holder.txtClassNumber.setText(String.valueOf(position + 1));
        holder.txtClassName.setText(aClass.getClazz().getName());
        holder.txtMajorName.setText(aClass.getMajor().getName());
        holder.txtAcademicYearName.setText(aClass.getAcademicYear().getName());
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public static class ClassStatisticalViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtClassNumber;
        private final TextView txtClassName;
        private final TextView txtMajorName;
        private final TextView txtAcademicYearName;

        public ClassStatisticalViewHolder(View itemView) {
            super(itemView);

            txtClassNumber = itemView.findViewById(R.id.txtClassNumber);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtMajorName = itemView.findViewById(R.id.txtMajorName);
            txtAcademicYearName = itemView.findViewById(R.id.txtAcademicYearName);
        }
    }
}
