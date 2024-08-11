package com.fh.app_student_management.adapters.lecturer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.listener.ItemClickListener;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.Score;
import com.fh.app_student_management.data.entities.Subject;
import com.fh.app_student_management.data.relations.StudentWithScores;
import com.fh.app_student_management.utilities.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ScoreListRecycleViewAdapter extends RecyclerView.Adapter<ScoreListRecycleViewAdapter.ScoreViewHolder> {

    private final Context context;
    private final Subject subject;
    private final long semesterId;
    private final ArrayList<StudentWithScores> originalList;

    private final AppDatabase db;

    public ScoreListRecycleViewAdapter(Context context, Intent intent, ArrayList<StudentWithScores> originalList) {
        this.context = context;
        this.originalList = originalList;

        db = AppDatabase.getInstance(context);
        long subjectId = intent.getLongExtra(Constants.SUBJECT_ID, 0);
        subject = db.subjectDAO().getById(subjectId);
        semesterId = intent.getLongExtra(Constants.SEMESTER_ID, 0);
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lecturer_layout_recycle_view_score, parent, false);

        return new ScoreViewHolder(view);
    }

    @Override
    @SuppressLint("InflateParams")
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        StudentWithScores student = originalList.get(position);

        holder.txtStudentId.setText(String.valueOf(student.getStudentId()));
        holder.txtStudentName.setText(student.getStudentName());
        holder.txtGK.setText(String.valueOf(student.getGkScore()));
        holder.txtCK.setText(String.valueOf(student.getCkScore()));
        holder.txtTB.setText(String.valueOf(student.getTbScore()));

        holder.setItemClickListener((view, position1, isLongClick) -> {
            View view1 = LayoutInflater.from(context).inflate(R.layout.lecturer_bottom_sheet_add_score, null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

            TextView edtStudentId = view1.findViewById(R.id.edtStudentId);
            TextView edtStudentName = view1.findViewById(R.id.edtStudentName);
            TextView edtSubjectName = view1.findViewById(R.id.edtSubjectName);
            EditText txtGK = view1.findViewById(R.id.edtGK);
            EditText txtCK = view1.findViewById(R.id.edtCK);
            Button btnAddPoint = view1.findViewById(R.id.btnAddScore);

            edtStudentId.setText(String.valueOf(student.getStudentId()));
            edtStudentName.setText(student.getStudentName());
            edtSubjectName.setText(subject.getName());
            txtGK.setText(String.valueOf(student.getGkScore()));
            txtCK.setText(String.valueOf(student.getCkScore()));

            btnAddPoint.setOnClickListener(v -> {
                String gk = txtGK.getText().toString();
                String ck = txtCK.getText().toString();

                if (gk.isEmpty() || ck.isEmpty()) {
                    return;
                }

                float gkScore = Float.parseFloat(gk);
                float ckScore = Float.parseFloat(ck);
                float tb = (gkScore + ckScore) / 2;

                List<Score> scores = db.scoreDAO().getByStudent(student.getStudentId(), subject.getId(), semesterId);
                if (scores.isEmpty()) {
                    db.scoreDAO().insert(new Score("GK", gkScore, student.getStudentId(), subject.getId(), semesterId));
                    db.scoreDAO().insert(new Score("CK", ckScore, student.getStudentId(), subject.getId(), semesterId));
                    db.scoreDAO().insert(new Score("TB", tb, student.getStudentId(), subject.getId(), semesterId));
                } else {
                    for (Score score : scores) {
                        switch (score.getType()) {
                            case "GK":
                                score.setPoint(gkScore);
                                break;
                            case "CK":
                                score.setPoint(ckScore);
                                break;
                            case "TB":
                                score.setPoint(tb);
                                break;
                        }
                        db.scoreDAO().update(score);
                    }
                }

                student.setGkScore(gkScore);
                student.setCkScore(ckScore);
                student.setTbScore(tb);
                originalList.set(position, student);
                bottomSheetDialog.dismiss();
                notifyItemChanged(position);
            });

            bottomSheetDialog.setContentView(view1);

            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            assert bottomSheet != null;
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);

            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setSkipCollapsed(true);

            bottomSheetDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return originalList.size();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView txtStudentId;
        private final TextView txtStudentName;
        private final TextView txtGK;
        private final TextView txtCK;
        private final TextView txtTB;
        private ItemClickListener itemClickListener;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStudentId = itemView.findViewById(R.id.txtStudentId);
            txtStudentName = itemView.findViewById(R.id.txtStudentName);
            txtGK = itemView.findViewById(R.id.txtGK);
            txtCK = itemView.findViewById(R.id.txtCK);
            txtTB = itemView.findViewById(R.id.txtTB);

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