package com.fh.app_student_management.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.listener.SemesterItemClickListener;
import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.ui.ClassActivity;
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
        View view = LayoutInflater.from(context).inflate(R.layout.layout_listview_semester, parent, false);

        return new SemesterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SemesterViewHolder holder, int position) {
        Semester semester = filteredList.get(position);
        String startDate = Constants.DATE_FORMAT("MM/yyyy").format(semester.getStartDate());
        String endDate = Constants.DATE_FORMAT("MM/yyyy").format(semester.getEndDate());
        String semesterName = String.format("%s (%s - %s)", semester.getName(), startDate, endDate);
        holder.txtSemesterName.setText(semesterName);

        holder.setSemesterItemClickListener((view, position1, isLongClick) -> {
            Long userId = intent.getLongExtra(Constants.USER_ID, 0);
            Intent newIntent = new Intent(context, ClassActivity.class);
            newIntent.putExtra(Constants.USER_ID, userId);
            newIntent.putExtra(Constants.SEMESTER_ID, semester.getId());
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
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase(Locale.getDefault());

                if (query.isEmpty()) {
                    filteredList = originalList;
                } else {
                    ArrayList<Semester> filtered = new ArrayList<>();
                    for (Semester semester : originalList) {
                        String startDate = Constants.DATE_FORMAT("MM/yyyy").format(semester.getStartDate());
                        String endDate = Constants.DATE_FORMAT("MM/yyyy").format(semester.getEndDate());
                        String semesterName = String.format("%s (%s - %s)", semester.getName(), startDate, endDate);
                        if (Utils.removeVietnameseAccents(semesterName.toLowerCase()).contains(Utils.removeVietnameseAccents(query))) {
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
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Semester>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class SemesterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView txtSemesterName;
        private SemesterItemClickListener semesterItemClickListener;

        public SemesterViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSemesterName = itemView.findViewById(R.id.semesterName);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setSemesterItemClickListener(SemesterItemClickListener semesterItemClickListener) {
            this.semesterItemClickListener = semesterItemClickListener;
        }

        @Override
        public void onClick(View view) {
            semesterItemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            semesterItemClickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}
