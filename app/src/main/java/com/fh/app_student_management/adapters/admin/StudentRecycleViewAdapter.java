package com.fh.app_student_management.adapters.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.listener.ItemClickListener;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.AcademicYear;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.relations.StudentWithRelations;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;
import com.fh.app_student_management.utilities.Validator;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class StudentRecycleViewAdapter extends RecyclerView.Adapter<StudentRecycleViewAdapter.StudentViewHolder> implements Filterable {

    private final Context context;
    private final AppDatabase db;
    private final ArrayList<StudentWithRelations> originalList;
    private final BottomSheetDialog bottomSheetDialog;
    private final ArrayList<Major> majors;
    private final ArrayList<String> majorNames;
    private final ArrayList<Class> classes;
    private final ArrayList<String> classNames;
    private final ArrayList<AcademicYear> academicYears;
    private final ArrayList<String> academicYearNames;

    private String currentFilterText = "";
    private Major selectedMajor;
    private Class selectedClass;
    private AcademicYear selectedAcademicYear;
    private ArrayList<StudentWithRelations> filteredList;

    public StudentRecycleViewAdapter(Context context, ArrayList<StudentWithRelations> originalList) {
        this.context = context;
        this.originalList = originalList;
        this.filteredList = originalList;

        db = AppDatabase.getInstance(context);
        bottomSheetDialog = new BottomSheetDialog(context);

        majors = new ArrayList<>(db.majorDAO().getAll());
        majorNames = new ArrayList<>(majors.size() + 1);
        majorNames.add(0, "--- Chọn ngành ---");
        for (int i = 0; i < majors.size(); i++) {
            majorNames.add(majors.get(i).getName());
        }

        classes = new ArrayList<>(db.classDAO().getAll());
        classNames = new ArrayList<>(classes.size() + 1);
        classNames.add(0, "--- Chọn lớp ---");
        for (int i = 0; i < classes.size(); i++) {
            classNames.add(classes.get(i).getName());
        }

        academicYears = new ArrayList<>(db.academicYearDAO().getAll());
        academicYearNames = new ArrayList<>(academicYears.size() + 1);
        academicYearNames.add(0, "--- Chọn khóa học ---");
        for (int i = 0; i < academicYears.size(); i++) {
            academicYearNames.add(academicYears.get(i).getName());
        }
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
    @SuppressLint("InflateParams")
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentWithRelations studentWithRelations = filteredList.get(position);
        holder.txtStudentId.setText(String.valueOf(studentWithRelations.getStudent().getId()));
        holder.txtStudentName.setText(studentWithRelations.getUser().getFullName());

        holder.setItemClickListener((view, position1, isLongClick) -> showEditStudentDialog(studentWithRelations));
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

    private void editStudent(int position) {
        StudentWithRelations studentWithRelations = filteredList.get(position);
        db.studentDAO().update(studentWithRelations.getStudent());
        db.userDAO().update(studentWithRelations.getUser());
        getFilter().filter(currentFilterText);
        notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        StudentWithRelations studentWithRelations = filteredList.get(position);
        db.studentDAO().delete(studentWithRelations.getStudent());
        db.userDAO().delete(studentWithRelations.getUser());
        originalList.remove(studentWithRelations);
        filteredList.remove(studentWithRelations);
        getFilter().filter(currentFilterText);
        notifyItemRemoved(position);
    }

    @SuppressLint("InflateParams")
    private void showEditStudentDialog(StudentWithRelations studentWithRelations) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_bottom_sheet_edit_student, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setSkipCollapsed(true);
        behavior.setDraggable(false);
        behavior.setHideable(true);

        selectedMajor = studentWithRelations.getMajor();
        selectedClass = studentWithRelations.getClazz();
        selectedAcademicYear = studentWithRelations.getAcademicYear();

        ImageView iconCamera = view.findViewById(R.id.iconCamera);
        ImageView avatar = view.findViewById(R.id.avatar);
        EditText edtEmail = view.findViewById(R.id.edtEmail);
        EditText edtFullName = view.findViewById(R.id.edtFullName);
        RadioGroup radioGroupGender = view.findViewById(R.id.radioGroupGender);
        EditText edtDob = view.findViewById(R.id.edtDob);
        EditText edtAddress = view.findViewById(R.id.edtAddress);
        EditText edtMajor = view.findViewById(R.id.edtMajor);
        EditText edtClass = view.findViewById(R.id.edtClass);
        EditText edtAcademicYear = view.findViewById(R.id.edtAcademicYear);
        Button btnEdit = view.findViewById(R.id.btnEditStudent);
        Button btnDelete = view.findViewById(R.id.btnDeleteStudent);

        avatar.setImageBitmap(Utils.getBitmapFromBytes(studentWithRelations.getUser().getAvatar()));
        edtEmail.setText(studentWithRelations.getUser().getEmail());
        edtFullName.setText(studentWithRelations.getUser().getFullName());
        if (studentWithRelations.getUser().getGender() == Constants.MALE) {
            radioGroupGender.check(R.id.radioButtonMale);
        } else {
            radioGroupGender.check(R.id.radioButtonFemale);
        }
        edtDob.setText(Utils.formatDate("dd/MM/YYYY").format(studentWithRelations.getUser().getDob()));
        edtAddress.setText(studentWithRelations.getUser().getAddress());
        edtMajor.setText(studentWithRelations.getMajor().getName());
        edtClass.setText(studentWithRelations.getClazz().getName());
        edtAcademicYear.setText(studentWithRelations.getAcademicYear().getName());

        iconCamera.setOnClickListener(v1 -> ImagePicker.with((Activity) context)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        edtDob.setOnClickListener(this::showDatePickerDialog);

        edtMajor.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Chọn lớp")
                .setItems(majorNames.toArray(new CharSequence[0]), (dialog, which) -> {
                    if (which == 0) {
                        edtMajor.setText("");
                        selectedMajor = null;
                        return;
                    }

                    selectedMajor = majors.get(which - 1);
                    ((EditText) view.findViewById(R.id.edtMajor)).setText(majorNames.get(which));
                })
                .show());

        edtClass.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Chọn ngành")
                .setItems(classNames.toArray(new CharSequence[0]), (dialog, which) -> {
                    if (which == 0) {
                        edtClass.setText("");
                        selectedClass = null;
                        return;
                    }

                    selectedClass = classes.get(which - 1);
                    ((EditText) view.findViewById(R.id.edtClass)).setText(classNames.get(which));
                })
                .show());

        edtAcademicYear.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Chọn năm học")
                .setItems(academicYearNames.toArray(new CharSequence[0]), (dialog, which) -> {
                    if (which == 0) {
                        edtAcademicYear.setText("");
                        selectedAcademicYear = null;
                        return;
                    }

                    selectedAcademicYear = academicYears.get(which - 1);
                    ((EditText) view.findViewById(R.id.edtAcademicYear)).setText(academicYearNames.get(which));
                })
                .show());

        btnEdit.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Xác nhận chỉnh sửa thông tin sinh viên?")
                .setPositiveButton("Có", (dialog, which) -> performEditStudent(studentWithRelations, view))
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show());

        btnDelete.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Xác nhận xóa sinh viên?")
                .setPositiveButton("Có", (dialog, which) -> {
                    deleteStudent(originalList.indexOf(studentWithRelations));
                    bottomSheetDialog.dismiss();
                    Utils.showToast(context, "Xóa thành công");
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show());

        bottomSheetDialog.show();
    }

    private void performEditStudent(StudentWithRelations studentWithRelations, View view) {
        if (!validateInputs(view)) return;

        ImageView avatar = view.findViewById(R.id.avatar);
        EditText edtFullName = view.findViewById(R.id.edtFullName);
        RadioGroup radioGroupGender = view.findViewById(R.id.radioGroupGender);
        EditText edtDob = view.findViewById(R.id.edtDob);
        EditText edtAddress = view.findViewById(R.id.edtAddress);

        try {
            studentWithRelations.getUser().setAvatar(Utils.getBytesFromBitmap(Utils.getBitmapFromView(avatar)));
            studentWithRelations.getUser().setFullName(edtFullName.getText().toString());
            studentWithRelations.getUser().setDob(Utils.formatDate("dd/MM/YYYY").parse(edtDob.getText().toString()));
            studentWithRelations.getUser().setAddress(edtAddress.getText().toString());
            studentWithRelations.getStudent().setMajorId(selectedMajor.getId());
            studentWithRelations.getStudent().setClassId(selectedClass.getId());
            studentWithRelations.getStudent().setAcademicYearId(selectedAcademicYear.getId());
            studentWithRelations.setMajor(selectedMajor);
            studentWithRelations.setClazz(selectedClass);
            studentWithRelations.setAcademicYear(selectedAcademicYear);

            int genderId = radioGroupGender.getCheckedRadioButtonId();
            studentWithRelations.getUser().setGender(genderId == R.id.radioButtonMale ? Constants.MALE : Constants.FEMALE);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        editStudent(originalList.indexOf(studentWithRelations));
        bottomSheetDialog.dismiss();
        Utils.showToast(context, "Sửa thành công");
    }

    private void showDatePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(context, (v, year, month, day) -> {
            String date = day + "/" + (month + 1) + "/" + year;
            ((TextView) view).setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private boolean validateInputs(View view) {
        return validateNotEmpty(view, R.id.edtEmail, "Email không được để trống")
                && validateEmail(view, R.id.edtEmail)
                && validateNotEmpty(view, R.id.edtFullName, "Họ và tên không được để trống")
                && validateNotEmpty(view, R.id.edtDob, "Ngày sinh không được để trống")
                && validateNotEmpty(view, R.id.edtAddress, "Địa chỉ không được để trống")
                && validateNotEmpty(view, R.id.edtMajor, "Ngành không được để trống")
                && validateNotEmpty(view, R.id.edtClass, "Lớp không được để trống")
                && validateNotEmpty(view, R.id.edtAcademicYear, "Năm học không được để trống");
    }

    private boolean validateNotEmpty(@NonNull View view, int viewId, String errorMessage) {
        EditText editText = view.findViewById(viewId);
        if (editText == null || editText.getText().toString().trim().isEmpty()) {
            Utils.showToast(context, errorMessage);
            return false;
        }
        return true;
    }

    private boolean validateEmail(@NonNull View view, int viewId) {
        EditText editText = view.findViewById(viewId);
        if (editText != null && !Validator.isValidEmail(editText.getText().toString())) {
            Utils.showToast(context, "Email không hợp lệ");
            return false;
        }
        return true;
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
