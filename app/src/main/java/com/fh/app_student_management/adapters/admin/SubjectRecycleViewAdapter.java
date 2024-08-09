package com.fh.app_student_management.adapters.admin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.relations.LecturerAndUser;
import com.fh.app_student_management.utilities.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class SubjectRecycleViewAdapter extends RecyclerView.Adapter<SubjectRecycleViewAdapter.SubjectViewHolder> implements Filterable {

    private final Context context;
    private final ArrayList<LecturerAndUser> originalList;
    private final AppDatabase db;
    private final BottomSheetDialog bottomSheetDialog;

    private ArrayList<LecturerAndUser> filteredList;

    public SubjectRecycleViewAdapter(Context context, ArrayList<LecturerAndUser> originalList) {
        this.context = context;
        this.originalList = originalList;
        this.filteredList = originalList;
        this.db = AppDatabase.getInstance(context);
        this.bottomSheetDialog = new BottomSheetDialog(context);
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtSubjectName;
        private final TextView txtClassName;
        private final TextView txtMajorName;
        private final TextView txtSubjectCredits;
        private final Button btnEditSubject;
        private final Button btnDeleteSubject;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSubjectName = itemView.findViewById(R.id.txtSubjectName);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtMajorName = itemView.findViewById(R.id.txtMajorName);
            txtSubjectCredits = itemView.findViewById(R.id.txtSubjectCredits);
            btnEditSubject = itemView.findViewById(R.id.btnEditSubject);
            btnDeleteSubject = itemView.findViewById(R.id.btnDeleteSubject);
        }
    }
}
