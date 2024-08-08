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
import com.fh.app_student_management.adapters.listener.ItemClickListener;
import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.ui.SubjectActivity;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;

import java.util.ArrayList;
import java.util.Locale;

public class SemesterRecyclerViewAdapter extends RecyclerView.Adapter<SemesterRecyclerViewAdapter.SemesterViewHolder> implements Filterable {

    private final Context context;
    private final Intent intent;
    private final ArrayList<Semester> originalList;
    private ArrayList<Semester> filteredList;

    public SemesterRecyclerViewAdapter(Context context, Intent intent, ArrayList<Semester> semesters) {
        this.context = context;
        this.intent = intent;
        this.originalList = semesters;
        this.filteredList = semesters;
    }

    @NonNull
    @Override
    public SemesterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recycle_view_semesters, parent, false);

        return new SemesterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SemesterViewHolder holder, int position) {
        Semester semester = filteredList.get(position);
        String startDate = Constants.DATE_FORMAT("MM/yyyy").format(semester.getStartDate());
        String endDate = Constants.DATE_FORMAT("MM/yyyy").format(semester.getEndDate());
        String semesterName = String.format("%s (%s - %s)", semester.getName(), startDate, endDate);
        holder.txtSemesterName.setText(semesterName);

        holder.setItemClickListener((view, position1, isLongClick) -> {
            Bundle bundle = intent.getExtras();

            assert bundle != null;
            long userId = bundle.getLong(Constants.USER_ID, 0);
            Intent newIntent = new Intent(context, SubjectActivity.class);
            Bundle newBundle = new Bundle();
            newBundle.putLong(Constants.USER_ID, userId);
            newBundle.putLong(Constants.SEMESTER_ID, semester.getId());
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
                    ArrayList<Semester> filtered = new ArrayList<>();
                    for (Semester semester : originalList) {
                        String startDate = Constants.DATE_FORMAT("MM/yyyy").format(semester.getStartDate());
                        String endDate = Constants.DATE_FORMAT("MM/yyyy").format(semester.getEndDate());
                        String semesterName = String.format("%s (%s - %s)", semester.getName(), startDate, endDate);
                        String semesterNameFilter = Utils.removeVietnameseAccents(semesterName.toLowerCase(Locale.getDefault()));

                        if (semesterNameFilter.contains(query)) {
                            filtered.add(semester);
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
                filteredList = (ArrayList<Semester>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class SemesterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ItemClickListener itemClickListener;
        private final TextView txtSemesterName;

        public SemesterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSemesterName = itemView.findViewById(R.id.semesterName);

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
