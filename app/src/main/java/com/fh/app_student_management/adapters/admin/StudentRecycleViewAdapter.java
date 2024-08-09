package com.fh.app_student_management.adapters.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
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
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.relations.StudentWithRelations;
import com.fh.app_student_management.utilities.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Locale;

public class StudentRecycleViewAdapter extends RecyclerView.Adapter<StudentRecycleViewAdapter.StudentViewHolder> implements Filterable {

    private final Context context;
    private final ArrayList<StudentWithRelations> originalList;
    private final AppDatabase db;
    private final BottomSheetDialog bottomSheetDialog;

    private String currentFilterText = "";
    private ArrayList<StudentWithRelations> filteredList;

    public StudentRecycleViewAdapter(Context context, ArrayList<StudentWithRelations> originalList) {
        this.context = context;
        this.originalList = originalList;
        this.filteredList = originalList;

        db = AppDatabase.getInstance(context);
        bottomSheetDialog = new BottomSheetDialog(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(ArrayList<StudentWithRelations> originalList) {
        this.filteredList = originalList;
        notifyDataSetChanged();
    }

    public void resetFilteredList() {
        setFilteredList(originalList);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_layout_recycle_view_list_student, parent, false);

        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentWithRelations studentWithRelations = filteredList.get(position);
        holder.txtStudentId.setText(String.valueOf(studentWithRelations.getStudent().getId()));
        holder.txtStudentName.setText(studentWithRelations.getUser().getFullName());

        holder.setItemClickListener((view, pos, isLongClick) -> {

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
                currentFilterText = charSequence.toString();
                String query = Utils.removeVietnameseAccents(charSequence.toString().toLowerCase(Locale.getDefault()));

                if (query.isEmpty()) {
                    filteredList = originalList;
                } else {
                    ArrayList<StudentWithRelations> filtered = new ArrayList<>();
                    for (StudentWithRelations studentWithRelations : originalList) {
                        String studentName = Utils.removeVietnameseAccents(studentWithRelations.getUser().getFullName().toLowerCase(Locale.getDefault()));

                        if (studentName.contains(query)) {
                            filtered.add(studentWithRelations);
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
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<StudentWithRelations>) filterResults.values;
                notifyItemRangeChanged(0, filteredList.size());
            }
        };
    }

    public void addStudent(StudentWithRelations studentWithRelations) {
        Long userId;
        try {
            userId = db.userDAO().insert(studentWithRelations.getUser());
        } catch (SQLiteConstraintException ex) {
            Utils.showToast(context, "Email đã tồn tại!");
            return;
        }
        studentWithRelations.getStudent().setUserId(userId);
        db.studentDAO().insert(studentWithRelations.getStudent());
        originalList.add(0, studentWithRelations);
        notifyItemInserted(0);
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView txtStudentId;
        private final TextView txtStudentName;
        private ItemClickListener itemClickListener;

        public StudentViewHolder(View itemView) {
            super(itemView);

            txtStudentId = itemView.findViewById(R.id.txtStudentId);
            txtStudentName = itemView.findViewById(R.id.txtStudentName);

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
