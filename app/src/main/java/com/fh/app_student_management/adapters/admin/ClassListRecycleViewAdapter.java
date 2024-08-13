package com.fh.app_student_management.adapters.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.fh.app_student_management.adapters.listener.ItemClickListener;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.AcademicYear;
import com.fh.app_student_management.data.entities.Faculty;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.relations.ClassWithRelations;
import com.fh.app_student_management.ui.StudentListActivity;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Locale;

public class ClassListRecycleViewAdapter extends RecyclerView.Adapter<ClassListRecycleViewAdapter.ClassViewHolder> implements Filterable {

    private final Context context;
    private final ArrayList<ClassWithRelations> originalList;
    private final BottomSheetDialog bottomSheetDialog;
    private final ArrayList<Major> majors;
    private final String[] majorNames;
    private final ArrayList<AcademicYear> academicYears;
    private final String[] academicYearNames;

    private String currentFilterText = "";
    private Major selectedMajor;
    private AcademicYear selectedAcademicYear;
    private ArrayList<ClassWithRelations> filteredList;

    public ClassListRecycleViewAdapter(Context context, ArrayList<ClassWithRelations> originalList) {
        this.context = context;
        this.originalList = originalList;
        this.filteredList = originalList;

        bottomSheetDialog = new BottomSheetDialog(context);

        majors = new ArrayList<>(AppDatabase.getInstance(context).majorDAO().getAll());
        majorNames = new String[majors.size()];
        for (int i = 0; i < majors.size(); i++) {
            majorNames[i] = majors.get(i).getName();
        }

        academicYears = new ArrayList<>(AppDatabase.getInstance(context).academicYearDAO().getAll());
        academicYearNames = new String[academicYears.size()];
        for (int i = 0; i < academicYears.size(); i++) {
            academicYearNames[i] = academicYears.get(i).getName();
        }
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_layout_recycle_view_list_class, parent, false);

        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassWithRelations classWithRelations = filteredList.get(position);
        Faculty faculty = AppDatabase.getInstance(context).facultyDAO().getById(classWithRelations.getMajor().getFacultyId());
        holder.txtClassName.setText(classWithRelations.getClazz().getName());
        holder.txtFacultyName.setText(faculty.getName());
        holder.txtMajorName.setText(classWithRelations.getMajor().getName());
        holder.txtAcademicYearName.setText(classWithRelations.getAcademicYear().getName());

        holder.setItemClickListener((view, position1, isLongClick) -> {
            Intent intent = new Intent(context, StudentListActivity.class);
            intent.putExtra("isStudentClass", true);
            intent.putExtra(Constants.CLASS_ID, classWithRelations.getClazz().getId());
            context.startActivity(intent);
        });

        holder.btnEditClass.setOnClickListener(v -> showEditClassDialog(classWithRelations));

        holder.btnDeleteClass.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Xác nhận xóa lớp học?")
                .setPositiveButton("Có", (dialog, which) -> deleteClass(originalList.indexOf(classWithRelations)))
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
                    ArrayList<ClassWithRelations> filtered = new ArrayList<>();
                    for (ClassWithRelations classWithRelations : originalList) {
                        String className = Utils.removeVietnameseAccents(classWithRelations.getClazz().getName().toLowerCase(Locale.getDefault()));

                        if (className.contains(query)) {
                            filtered.add(classWithRelations);
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
                filteredList = (ArrayList<ClassWithRelations>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void addClass(ClassWithRelations classWithRelations) {
        AppDatabase.getInstance(context).classDAO().insert(classWithRelations.getClazz());
        originalList.add(0, classWithRelations);
        notifyItemInserted(0);
    }

    private void editClass(int position) {
        ClassWithRelations classWithRelations = filteredList.get(position);
        AppDatabase.getInstance(context).classDAO().update(classWithRelations.getClazz());
        getFilter().filter(currentFilterText);
        notifyItemChanged(position);
    }

    private void deleteClass(int position) {
        ClassWithRelations classWithRelations = filteredList.get(position);
        AppDatabase.getInstance(context).classDAO().delete(classWithRelations.getClazz());
        originalList.remove(classWithRelations);
        filteredList.remove(classWithRelations);
        getFilter().filter(currentFilterText);
        notifyItemRemoved(position);
    }

    @SuppressLint("InflateParams")
    private void showEditClassDialog(ClassWithRelations classWithRelations) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_bottom_sheet_edit_class, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setSkipCollapsed(true);
        bottomSheetDialog.show();

        selectedMajor = classWithRelations.getMajor();
        selectedAcademicYear = classWithRelations.getAcademicYear();

        EditText edtClassName = view.findViewById(R.id.edtClassName);
        EditText edtMajorName = view.findViewById(R.id.edtMajor);
        EditText edtAcademicYearName = view.findViewById(R.id.edtAcademicYear);
        Button btnEdit = view.findViewById(R.id.btnEditClass);

        edtClassName.setText(classWithRelations.getClazz().getName());
        edtMajorName.setText(classWithRelations.getMajor().getName());
        edtAcademicYearName.setText(classWithRelations.getAcademicYear().getName());

        edtMajorName.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Chọn ngành")
                .setItems(majorNames, (dialog, which) -> {
                    selectedMajor = majors.get(which);
                    ((EditText) view.findViewById(R.id.edtMajor)).setText(majorNames[which]);
                })
                .show());

        edtAcademicYearName.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Chọn khóa học")
                .setItems(academicYearNames, (dialog, which) -> {
                    selectedAcademicYear = academicYears.get(which);
                    ((EditText) view.findViewById(R.id.edtAcademicYear)).setText(academicYearNames[which]);
                })
                .show());

        btnEdit.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Xác nhận chỉnh sửa thông tin lớp học?")
                .setPositiveButton("Có", (dialog, which) -> performEditClass(classWithRelations, view))
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show());
    }

    private void performEditClass(ClassWithRelations classWithRelations, View view) {
        if (!validateInputs(view)) return;

        EditText edtClassName = view.findViewById(R.id.edtClass);
        EditText edtMajorName = view.findViewById(R.id.edtMajor);
        EditText edtAcademicYearName = view.findViewById(R.id.edtAcademicYear);

        classWithRelations.getClazz().setName(edtClassName.getText().toString());
        classWithRelations.getMajor().setName(edtMajorName.getText().toString());
        classWithRelations.getAcademicYear().setName(edtAcademicYearName.getText().toString());
        classWithRelations.setMajor(selectedMajor);
        classWithRelations.setAcademicYear(selectedAcademicYear);

        editClass(originalList.indexOf(classWithRelations));
        bottomSheetDialog.dismiss();
        Utils.showToast(context, "Sửa thành công");
    }

    private boolean validateInputs(View view) {
        return validateNotEmpty(view, R.id.edtClass, "Tên lớp không được để trống")
                && validateNotEmpty(view, R.id.edtMajor, "Ngành không được để trống")
                && validateNotEmpty(view, R.id.edtAcademicYear, "Năm học không được để trống");
    }

    private boolean validateNotEmpty(View view, int viewId, String errorMessage) {
        EditText editText = view.findViewById(viewId);
        if (editText == null || editText.getText().toString().trim().isEmpty()) {
            Utils.showToast(context, errorMessage);
            return false;
        }
        return true;
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView txtClassName;
        private final TextView txtFacultyName;
        private final TextView txtMajorName;
        private final TextView txtAcademicYearName;
        private final Button btnEditClass;
        private final Button btnDeleteClass;
        private ItemClickListener itemClickListener;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);

            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtFacultyName = itemView.findViewById(R.id.txtFacultyName);
            txtMajorName = itemView.findViewById(R.id.txtMajorName);
            txtAcademicYearName = itemView.findViewById(R.id.txtAcademicYearName);
            btnEditClass = itemView.findViewById(R.id.btnEditClass);
            btnDeleteClass = itemView.findViewById(R.id.btnDeleteClass);

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
