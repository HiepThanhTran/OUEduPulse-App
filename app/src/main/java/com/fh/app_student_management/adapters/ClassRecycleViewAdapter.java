package com.fh.app_student_management.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.entities.ClassItemRecycleView;
import com.fh.app_student_management.ui.SubjectActivity;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;

import java.util.ArrayList;
import java.util.Locale;

public class ClassRecycleViewAdapter extends RecyclerView.Adapter<ClassRecycleViewAdapter.ClassViewHolder> implements Filterable {

    private final Context context;
    private final Intent intent;
    private final ArrayList<ClassItemRecycleView> originalList;
    private ArrayList<ClassItemRecycleView> filteredList;

    public ClassRecycleViewAdapter(Context context, Intent intent, ArrayList<ClassItemRecycleView> classes) {
        this.context = context;
        this.intent = intent;
        this.originalList = classes;
        this.filteredList = classes;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recycle_view_class, parent, false);

        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassItemRecycleView aClass = filteredList.get(position);
        holder.txtClassName.setText(aClass.getClassName());
        holder.txtFacultyName.setText(aClass.getFacultyName());
        holder.txtMajorName.setText(aClass.getMajorName());
        holder.txtAcademicYearName.setText(aClass.getAcademicYearName());

        holder.btnWatchPointClass.setOnClickListener(v -> {
            long userId = intent.getLongExtra(Constants.USER_ID, 0);
            Intent newIntent = new Intent(context, SubjectActivity.class);
            Bundle newBundle = new Bundle();
            newBundle.putLong(Constants.USER_ID, userId);
            newBundle.putLong(Constants.CLASS_ID, aClass.getId());
            newBundle.putLong(Constants.SEMESTER_ID, aClass.getSemesterId());
            newIntent.putExtras(newBundle);
            context.startActivity(newIntent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = Utils.removeVietnameseAccents(charSequence.toString().toLowerCase(Locale.getDefault()));

                if (query.isEmpty()) {
                    filteredList = originalList;
                } else {
                    ArrayList<ClassItemRecycleView> filtered = new ArrayList<>();
                    for (ClassItemRecycleView aClass : originalList) {
                        String className = Utils.removeVietnameseAccents(aClass.getClassName().toLowerCase(Locale.getDefault()));
                        String facultyName = Utils.removeVietnameseAccents(aClass.getFacultyName().toLowerCase(Locale.getDefault()));
                        String majorName = Utils.removeVietnameseAccents(aClass.getMajorName().toLowerCase(Locale.getDefault()));
                        String academicYearName = Utils.removeVietnameseAccents(aClass.getAcademicYearName().toLowerCase(Locale.getDefault()));

                        if (className.contains(query) || facultyName.contains(query) || majorName.contains(query) || academicYearName.contains(query)) {
                            filtered.add(aClass);
                        }
                    }
                    filteredList = filtered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }

            @Override
            @SuppressWarnings("unchecked")
            @SuppressLint("NotifyDataSetChanged")
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<ClassItemRecycleView>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtClassName;
        private final TextView txtFacultyName;
        private final TextView txtMajorName;
        private final TextView txtAcademicYearName;
        private final Button btnWatchPointClass;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtFacultyName = itemView.findViewById(R.id.txtFacultyName);
            txtMajorName = itemView.findViewById(R.id.txtMajorName);
            txtAcademicYearName = itemView.findViewById(R.id.txtAcademicYearName);
            btnWatchPointClass = itemView.findViewById(R.id.btnWatchPointClass);
        }
    }
}
