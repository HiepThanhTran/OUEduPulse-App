package com.fh.app_student_management.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.entities.SubjectItemRecycleView;
import com.fh.app_student_management.adapters.listener.ItemClickListener;
import com.fh.app_student_management.ui.ScoreActivity;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;

import java.util.ArrayList;
import java.util.Locale;

public class SubjectRecycleViewAdapter extends RecyclerView.Adapter<SubjectRecycleViewAdapter.SubjectViewHolder> implements Filterable {

    private final Context context;
    private final Intent intent;
    private final ArrayList<SubjectItemRecycleView> originalList;
    private ArrayList<SubjectItemRecycleView> filteredList;

    public SubjectRecycleViewAdapter(Context context, Intent intent, ArrayList<SubjectItemRecycleView> subjects) {
        this.context = context;
        this.intent = intent;
        this.originalList = subjects;
        this.filteredList = subjects;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lecturer_layout_recycle_view_subject, parent, false);

        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        SubjectItemRecycleView subjectItemRecycleView = filteredList.get(position);
        holder.txtSubjectName.setText(subjectItemRecycleView.getSubject().getName());
        holder.txtClassName.setText(subjectItemRecycleView.getClazz().getName());
        holder.txtMajorName.setText(subjectItemRecycleView.getMajor().getName());
        holder.txtCredits.setText(String.valueOf(subjectItemRecycleView.getSubject().getCredits()));

        holder.setItemClickListener((view, position1, isLongClick) -> {
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            long semesterId = bundle.getLong(Constants.SEMESTER_ID, 0);
            long subjectId = filteredList.get(position1).getSubject().getId();
            String className = filteredList.get(position1).getClazz().getName();
            Intent intent = new Intent(context, ScoreActivity.class);
            intent.putExtra(Constants.SEMESTER_ID, semesterId);
            intent.putExtra(Constants.SUBJECT_ID, subjectId);
            intent.putExtra("className", className);
            context.startActivity(intent);
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
                    ArrayList<SubjectItemRecycleView> filtered = new ArrayList<>();
                    for (SubjectItemRecycleView itemRecycleView : originalList) {
                        String subjectName = Utils.removeVietnameseAccents(itemRecycleView.getSubject().getName().toLowerCase(Locale.getDefault()));

                        if (subjectName.contains(query)) {
                            filtered.add(itemRecycleView);
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
                filteredList = (ArrayList<SubjectItemRecycleView>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView txtSubjectName;
        private final TextView txtClassName;
        private final TextView txtMajorName;
        private final TextView txtCredits;
        private ItemClickListener itemClickListener;

        public SubjectViewHolder(View itemView) {
            super(itemView);
            txtSubjectName = itemView.findViewById(R.id.txtSubjectName);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtMajorName = itemView.findViewById(R.id.txtMajorName);
            txtCredits = itemView.findViewById(R.id.txtCredits);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}
