package com.fh.app_student_management.adapters.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.relations.SubjectWithRelations;
import com.fh.app_student_management.utilities.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Locale;

public class SubjectListRecycleViewAdapter extends RecyclerView.Adapter<SubjectListRecycleViewAdapter.SubjectViewHolder> implements Filterable {

    private final Context context;
    private final ArrayList<SubjectWithRelations> originalList;
    private final BottomSheetDialog bottomSheetDialog;
    private final ArrayList<Class> classes;
    private final String[] classNames;
    private final ArrayList<Major> majors;
    private final String[] majorNames;

    private String currentFilterText = "";
    private Class selectedClass;
    private Major selectedMajor;
    private ArrayList<SubjectWithRelations> filteredList;

    public SubjectListRecycleViewAdapter(Context context, ArrayList<SubjectWithRelations> originalList) {
        this.context = context;
        this.originalList = originalList;
        this.filteredList = originalList;

        bottomSheetDialog = new BottomSheetDialog(context);

        classes = new ArrayList<>(AppDatabase.getInstance(context).classDAO().getAll());
        classNames = new String[classes.size()];
        for (int i = 0; i < classes.size(); i++) {
            classNames[i] = classes.get(i).getName();
        }

        majors = new ArrayList<>(AppDatabase.getInstance(context).majorDAO().getAll());
        majorNames = new String[majors.size()];
        for (int i = 0; i < majors.size(); i++) {
            majorNames[i] = majors.get(i).getName();
        }
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_layout_recycle_view_list_subject, parent, false);

        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        SubjectWithRelations subjectWithRelations = filteredList.get(position);
        holder.txtSubjectName.setText(subjectWithRelations.getSubject().getName());
        holder.txtClassName.setText(subjectWithRelations.getClazz().getName());
        holder.txtMajorName.setText(subjectWithRelations.getMajor().getName());
        holder.txtSubjectCredits.setText(String.valueOf(subjectWithRelations.getSubject().getCredits()));

        holder.btnEditSubject.setOnClickListener(v -> showEditSubjectDialog(subjectWithRelations));

        holder.btnDeleteSubject.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Xác nhận xóa môn học?")
                .setPositiveButton("Có", (dialog, which) -> deleteSubject(originalList.indexOf(subjectWithRelations)))
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show());
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
                    ArrayList<SubjectWithRelations> filtered = new ArrayList<>();
                    for (SubjectWithRelations subjectWithRelations : originalList) {
                        String subjectName = Utils.removeVietnameseAccents(subjectWithRelations.getSubject().getName().toLowerCase(Locale.getDefault()));

                        if (subjectName.contains(query)) {
                            filtered.add(subjectWithRelations);
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
                filteredList = (ArrayList<SubjectWithRelations>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void addSubject(SubjectWithRelations subjectWithRelations) {
        AppDatabase.getInstance(context).subjectDAO().insert(subjectWithRelations.getSubject());
        originalList.add(0, subjectWithRelations);
        notifyItemInserted(0);
    }

    private void editSubject(int position) {
        SubjectWithRelations subjectWithRelations = filteredList.get(position);
        AppDatabase.getInstance(context).subjectDAO().update(subjectWithRelations.getSubject());
        getFilter().filter(currentFilterText);
        notifyItemChanged(position);
    }

    private void deleteSubject(int position) {
        SubjectWithRelations subjectWithRelations = filteredList.get(position);
        AppDatabase.getInstance(context).subjectDAO().delete(subjectWithRelations.getSubject());
        originalList.remove(subjectWithRelations);
        filteredList.remove(subjectWithRelations);
        getFilter().filter(currentFilterText);
        notifyItemRemoved(position);
    }

    @SuppressLint("InflateParams")
    private void showEditSubjectDialog(SubjectWithRelations subjectWithRelations) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_bottom_sheet_edit_subject, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setSkipCollapsed(true);
        bottomSheetDialog.show();

        selectedClass = subjectWithRelations.getClazz();
        selectedMajor = subjectWithRelations.getMajor();

        EditText edtSubjectName = view.findViewById(R.id.edtSubjectName);
        EditText edtSubjectCredits = view.findViewById(R.id.edtSubjectCredits);
        EditText edtClassName = view.findViewById(R.id.edtClass);
        EditText edtMajorName = view.findViewById(R.id.edtMajor);
        Button btnEdit = view.findViewById(R.id.btnEditSubject);

        edtSubjectName.setText(subjectWithRelations.getSubject().getName());
        edtSubjectCredits.setText(String.valueOf(subjectWithRelations.getSubject().getCredits()));
        edtClassName.setText(subjectWithRelations.getClazz().getName());
        edtMajorName.setText(subjectWithRelations.getMajor().getName());

        edtClassName.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Chọn lớp")
                .setItems(classNames, (dialog, which) -> {
                    selectedClass = classes.get(which);
                    ((EditText) view.findViewById(R.id.edtClass)).setText(classNames[which]);
                })
                .show());

        edtMajorName.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Chọn ngành")
                .setItems(majorNames, (dialog, which) -> {
                    selectedMajor = majors.get(which);
                    ((EditText) view.findViewById(R.id.edtMajor)).setText(majorNames[which]);
                })
                .show());

        btnEdit.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Xác nhận chỉnh sửa thông tin môn học?")
                .setPositiveButton("Có", (dialog, which) -> performEditSubject(subjectWithRelations, view))
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show());
    }

    private void performEditSubject(SubjectWithRelations subjectWithRelations, View view) {
        if (!validateInputs(view)) return;

        EditText edtSubjectName = view.findViewById(R.id.edtSubjectName);
        EditText edtSubjectCredits = view.findViewById(R.id.edtSubjectCredits);

        subjectWithRelations.getSubject().setName(edtSubjectName.getText().toString());
        subjectWithRelations.getSubject().setCredits(Integer.parseInt(edtSubjectCredits.getText().toString()));
        subjectWithRelations.getSubject().setClassId(selectedClass.getId());
        subjectWithRelations.getSubject().setMajorId(selectedMajor.getId());
        subjectWithRelations.setClazz(selectedClass);
        subjectWithRelations.setMajor(selectedMajor);

        editSubject(originalList.indexOf(subjectWithRelations));
        bottomSheetDialog.dismiss();
        Utils.showToast(context, "Sửa thành công");
    }

    private boolean validateInputs(View view) {
        return validateNotEmpty(view, R.id.edtSubjectName, "Tên môn không được để trống")
                && validateNotEmpty(view, R.id.edtSubjectCredits, "Số tín chỉ không được để trống")
                && validateNotEmpty(view, R.id.edtClass, "Lớp không được để trống")
                && validateNotEmpty(view, R.id.edtMajor, "Ngành không được để trống");
    }

    private boolean validateNotEmpty(View view, int viewId, String errorMessage) {
        EditText editText = view.findViewById(viewId);
        if (editText == null || editText.getText().toString().trim().isEmpty()) {
            Utils.showToast(context, errorMessage);
            return false;
        }
        return true;
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtSubjectName;
        private final TextView txtClassName;
        private final TextView txtMajorName;
        private final TextView txtSubjectCredits;
        private final Button btnEditSubject;
        private final Button btnDeleteSubject;

        public SubjectViewHolder(View itemView) {
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