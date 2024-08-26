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
import com.fh.app_student_management.data.entities.Student;
import com.fh.app_student_management.data.entities.StudentSemesterCrossRef;
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

    public ScoreListRecycleViewAdapter(Context context, Intent intent, ArrayList<StudentWithScores> originalList) {
        this.context = context;
        this.originalList = originalList;

        long subjectId = intent.getLongExtra(Constants.SUBJECT_ID, 0);
        subject = AppDatabase.getInstance(context).subjectDAO().getById(subjectId);
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
        StudentWithScores studentWithScores = originalList.get(position);

        holder.txtStudentId.setText(String.valueOf(studentWithScores.getStudentId()));
        holder.txtStudentName.setText(studentWithScores.getStudentName());
        holder.txtGK.setText(String.valueOf(studentWithScores.getGkScore()));
        holder.txtCK.setText(String.valueOf(studentWithScores.getCkScore()));
        holder.txtTB.setText(String.valueOf(studentWithScores.getTbScore()));

        holder.setItemClickListener((view, position1, isLongClick) -> {
            View view1 = LayoutInflater.from(context).inflate(R.layout.lecturer_bottom_sheet_add_score, null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

            TextView edtStudentId = view1.findViewById(R.id.edtStudentId);
            TextView edtStudentName = view1.findViewById(R.id.edtStudentName);
            TextView edtSubjectName = view1.findViewById(R.id.edtSubjectName);
            EditText txtGK = view1.findViewById(R.id.edtGK);
            EditText txtCK = view1.findViewById(R.id.edtCK);
            Button btnAddPoint = view1.findViewById(R.id.btnAddScore);

            edtStudentId.setText(String.valueOf(studentWithScores.getStudentId()));
            edtStudentName.setText(studentWithScores.getStudentName());
            edtSubjectName.setText(subject.getName());
            txtGK.setText(String.valueOf(studentWithScores.getGkScore()));
            txtCK.setText(String.valueOf(studentWithScores.getCkScore()));

            btnAddPoint.setOnClickListener(v -> {
                String gk = txtGK.getText().toString();
                String ck = txtCK.getText().toString();

                if (gk.isEmpty() || ck.isEmpty()) {
                    return;
                }

                float gkScore = Float.parseFloat(gk);
                float ckScore = Float.parseFloat(ck);
                float tb = (gkScore + ckScore) / 2;

                List<Score> scores = AppDatabase.getInstance(context)
                        .scoreDAO().getByStudent(semesterId, subject.getId(), studentWithScores.getStudentId());

                boolean hasGK = false, hasCK = false, hasTB = false;

                for (Score score : scores) {
                    switch (score.getType()) {
                        case "GK":
                            score.setPoint(gkScore);
                            hasGK = true;
                            break;
                        case "CK":
                            score.setPoint(ckScore);
                            hasCK = true;
                            break;
                        case "TB":
                            score.setPoint(tb);
                            hasTB = true;
                            break;
                    }
                    AppDatabase.getInstance(context).scoreDAO().update(score);
                }

                if (!hasGK) {
                    AppDatabase.getInstance(context)
                            .scoreDAO().insert(new Score("GK", gkScore, studentWithScores.getStudentId(), subject.getId(), semesterId));
                }
                if (!hasCK) {
                    AppDatabase.getInstance(context)
                            .scoreDAO().insert(new Score("CK", ckScore, studentWithScores.getStudentId(), subject.getId(), semesterId));
                }
                if (!hasTB) {
                    AppDatabase.getInstance(context)
                            .scoreDAO().insert(new Score("TB", tb, studentWithScores.getStudentId(), subject.getId(), semesterId));
                }

                Student student = AppDatabase.getInstance(context).studentDAO().getById(studentWithScores.getStudentId());
                updateGPA(student, tb, subject.getCredits());
                AppDatabase.getInstance(context).studentDAO().update(student);

                StudentSemesterCrossRef studentSemesterCrossRef = AppDatabase.getInstance(context)
                        .crossRefDAO().getStudentSemesterCrossRef(studentWithScores.getStudentId(), semesterId);
                if (studentSemesterCrossRef != null) {
                    updateGPA(studentSemesterCrossRef, tb, subject.getCredits());
                    AppDatabase.getInstance(context).crossRefDAO().updateStudentSemesterCrossRef(studentSemesterCrossRef);
                } else {
                    studentSemesterCrossRef = new StudentSemesterCrossRef();
                    studentSemesterCrossRef.setStudentId(studentWithScores.getStudentId());
                    studentSemesterCrossRef.setSemesterId(semesterId);
                    studentSemesterCrossRef.setGpa(tb / subject.getCredits());
                    studentSemesterCrossRef.setTotalCredits(subject.getCredits());
                    AppDatabase.getInstance(context).crossRefDAO().insertStudentSemesterCrossRef(studentSemesterCrossRef);
                }

                studentWithScores.setGkScore(gkScore);
                studentWithScores.setCkScore(ckScore);
                studentWithScores.setTbScore(tb);
                originalList.set(position, studentWithScores);
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

    private void updateGPA(@NonNull Student student, float tb, float credits) {
        float newTotalCredits = student.getTotalCredits() + credits;
        float newTotalScore = (student.getGpa() * student.getTotalCredits()) + (tb * credits);
        student.setGpa(newTotalScore / newTotalCredits);
        student.setTotalCredits(newTotalCredits);
    }

    private void updateGPA(@NonNull StudentSemesterCrossRef studentSemesterCrossRef, float tb, float credits) {
        float newTotalCredits = studentSemesterCrossRef.getTotalCredits() + credits;
        float newTotalScore = (studentSemesterCrossRef.getGpa() * studentSemesterCrossRef.getTotalCredits()) + (tb * credits);
        studentSemesterCrossRef.setGpa(newTotalScore / newTotalCredits);
        studentSemesterCrossRef.setTotalCredits(newTotalCredits);
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