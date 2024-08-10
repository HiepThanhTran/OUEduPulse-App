package com.fh.app_student_management.adapters.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
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
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.relations.LecturerAndUser;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LecturerRecycleViewAdapter extends RecyclerView.Adapter<LecturerRecycleViewAdapter.LecturerViewHolder> implements Filterable {

    private final Context context;
    private final ArrayList<LecturerAndUser> originalList;
    private final AppDatabase db;
    private final BottomSheetDialog bottomSheetDialog;

    private String currentFilterText = "";
    private Constants.Role selectedRole;
    private ArrayList<LecturerAndUser> filteredList;

    public LecturerRecycleViewAdapter(Context context, ArrayList<LecturerAndUser> originalList) {
        this.context = context;
        this.originalList = originalList;
        this.filteredList = originalList;

        db = AppDatabase.getInstance(context);
        bottomSheetDialog = new BottomSheetDialog(context);
    }

    @NonNull
    @Override
    public LecturerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_layout_recycle_view_list_lecturer, parent, false);

        return new LecturerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LecturerViewHolder holder, int position) {
        LecturerAndUser lecturerAndUser = filteredList.get(position);
        holder.avatar.setImageBitmap(Utils.getBitmapFromBytes(lecturerAndUser.getUser().getAvatar()));
        holder.txtUsername.setText(lecturerAndUser.getUser().getFullName());
        holder.txtSpecialization.setText(lecturerAndUser.getLecturer().getSpecialization());

        holder.btnEditLecturer.setOnClickListener(v -> showEditLecturerDialog(lecturerAndUser));

        holder.btnDeleteLecturer.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Xác nhận xóa giảng viên?")
                .setPositiveButton("Có", (dialog, which) -> deleteLecturer(originalList.indexOf(lecturerAndUser)))
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
                    ArrayList<LecturerAndUser> filtered = new ArrayList<>();
                    for (LecturerAndUser lecturerAndUser : originalList) {
                        String lecturerName = Utils.removeVietnameseAccents(lecturerAndUser.getUser().getFullName().toLowerCase(Locale.getDefault()));

                        if (lecturerName.contains(query)) {
                            filtered.add(lecturerAndUser);
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
                filteredList = (ArrayList<LecturerAndUser>) filterResults.values;
                notifyItemRangeChanged(0, filteredList.size());
            }
        };
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            ImageView avatar = bottomSheetDialog.findViewById(R.id.avatar);
            if (avatar != null) {
                avatar.setImageURI(uri);
            }
        }
    }

    public void addLecturer(LecturerAndUser lecturerAndUser) {
        Long userId;
        try {
            userId = db.userDAO().insert(lecturerAndUser.getUser());
        } catch (SQLiteConstraintException ex) {
            Utils.showToast(context, "Email đã tồn tại!");
            return;
        }
        lecturerAndUser.getLecturer().setUserId(userId);
        db.lecturerDAO().insert(lecturerAndUser.getLecturer());
        originalList.add(0, lecturerAndUser);
        notifyItemInserted(0);
    }

    private void editLecturer(int position) {
        LecturerAndUser lecturerAndUser = filteredList.get(position);
        db.lecturerDAO().update(lecturerAndUser.getLecturer());
        db.userDAO().update(lecturerAndUser.getUser());
        getFilter().filter(currentFilterText);
        notifyItemChanged(position);
    }

    private void deleteLecturer(int position) {
        LecturerAndUser lecturerAndUser = filteredList.get(position);
        db.lecturerDAO().delete(lecturerAndUser.getLecturer());
        db.userDAO().delete(lecturerAndUser.getUser());
        originalList.remove(lecturerAndUser);
        filteredList.remove(lecturerAndUser);
        getFilter().filter(currentFilterText);
        notifyItemRemoved(position);
    }

    @SuppressLint("InflateParams")
    private void showEditLecturerDialog(LecturerAndUser lecturerAndUser) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_bottom_sheet_edit_lecturer, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setSkipCollapsed(true);
        behavior.setDraggable(false);
        behavior.setHideable(true);

        ImageView iconCamera = view.findViewById(R.id.iconCamera);
        ImageView avatar = view.findViewById(R.id.avatar);
        EditText edtEmail = view.findViewById(R.id.edtEmail);
        EditText edtFullName = view.findViewById(R.id.edtFullName);
        RadioGroup radioGroupGender = view.findViewById(R.id.radioGroupGender);
        EditText edtDob = view.findViewById(R.id.edtDob);
        EditText edtAddress = view.findViewById(R.id.edtAddress);
        EditText edtSpecialization = view.findViewById(R.id.edtSpecialization);
        EditText edtDegree = view.findViewById(R.id.edtDegree);
        EditText edtCertificate = view.findViewById(R.id.edtCertificate);
        TextView txtRole = view.findViewById(R.id.txtRole);
        Button btnEdit = view.findViewById(R.id.btnEditLecturer);

        avatar.setImageBitmap(Utils.getBitmapFromBytes(lecturerAndUser.getUser().getAvatar()));
        edtEmail.setText(lecturerAndUser.getUser().getEmail());
        edtFullName.setText(lecturerAndUser.getUser().getFullName());
        if (lecturerAndUser.getUser().getGender() == Constants.MALE) {
            radioGroupGender.check(R.id.radioButtonMale);
        } else {
            radioGroupGender.check(R.id.radioButtonFemale);
        }
        edtDob.setText(Utils.formatDate("dd/MM/YYYY").format(lecturerAndUser.getUser().getDob()));
        edtAddress.setText(lecturerAndUser.getUser().getAddress());
        edtSpecialization.setText(lecturerAndUser.getLecturer().getSpecialization());
        edtDegree.setText(lecturerAndUser.getLecturer().getDegree());
        edtCertificate.setText(lecturerAndUser.getLecturer().getCertificate());
        txtRole.setText(Utils.getRoleName(lecturerAndUser.getUser().getRole()));

        iconCamera.setOnClickListener(v1 -> ImagePicker.with((Activity) context)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        edtDob.setOnClickListener(this::showDatePickerDialog);

        String[] roleNames = {"Quản trị viên", "Giảng viên", "Sinh viên"};
        txtRole.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Chọn vai trò")
                .setItems(roleNames, (dialog, which) -> {
                    txtRole.setText(roleNames[which]);

                    switch (which) {
                        case 0:
                            selectedRole = Constants.Role.ADMIN;
                            break;
                        case 1:
                            selectedRole = Constants.Role.LECTURER;
                            break;
                        default:
                            selectedRole = Constants.Role.STUDENT;
                    }
                })
                .show());

        btnEdit.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Xác nhận chỉnh sửa thông tin giảng viên?")
                .setPositiveButton("Có", (dialog, which) -> performEditLecturer(lecturerAndUser, view))
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show());

        bottomSheetDialog.show();
    }

    private void performEditLecturer(LecturerAndUser lecturerAndUser, View view) {
        if (!validateInputs(view)) return;

        ImageView avatar = view.findViewById(R.id.avatar);
        EditText edtFullName = view.findViewById(R.id.edtFullName);
        RadioGroup radioGroupGender = view.findViewById(R.id.radioGroupGender);
        EditText edtDob = view.findViewById(R.id.edtDob);
        EditText edtAddress = view.findViewById(R.id.edtAddress);
        EditText edtSpecialization = view.findViewById(R.id.edtSpecialization);
        EditText edtDegree = view.findViewById(R.id.edtDegree);
        EditText edtCertificate = view.findViewById(R.id.edtCertificate);

        try {
            // TODO: Thêm khoa vào user, giảng viên làm việc ở khoa nào?
            lecturerAndUser.getUser().setAvatar(Utils.getBytesFromBitmap(Utils.getBitmapFromView(avatar)));
            lecturerAndUser.getUser().setFullName(edtFullName.getText().toString());
            lecturerAndUser.getUser().setDob(Utils.formatDate("dd/MM/YYYY").parse(edtDob.getText().toString()));
            lecturerAndUser.getUser().setAddress(edtAddress.getText().toString());
            lecturerAndUser.getUser().setRole(selectedRole);
            lecturerAndUser.getLecturer().setSpecialization(edtSpecialization.getText().toString());
            lecturerAndUser.getLecturer().setDegree(edtDegree.getText().toString());
            lecturerAndUser.getLecturer().setCertificate(edtCertificate.getText().toString());

            int genderId = radioGroupGender.getCheckedRadioButtonId();
            lecturerAndUser.getUser().setGender(genderId == R.id.radioButtonMale ? Constants.MALE : Constants.FEMALE);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        editLecturer(originalList.indexOf(lecturerAndUser));
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
                && validateNotEmpty(view, R.id.edtFullName, "Họ và tên không được để trống")
                && validateNotEmpty(view, R.id.edtDob, "Ngày sinh không được để trống")
                && validateNotEmpty(view, R.id.edtAddress, "Địa chỉ không được để trống")
                && validateNotEmpty(view, R.id.edtSpecialization, "Chuyên ngành không được để trống")
                && validateNotEmpty(view, R.id.edtDegree, "Bằng cấp không được để trống")
                && validateNotEmpty(view, R.id.edtCertificate, "Chứng chỉ không được để trống");
    }

    private boolean validateNotEmpty(View view, int viewId, String errorMessage) {
        EditText editText = view.findViewById(viewId);
        if (editText == null || editText.getText().toString().trim().isEmpty()) {
            Utils.showToast(context, errorMessage);
            return false;
        }
        return true;
    }

    public static class LecturerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView avatar;
        private final TextView txtUsername;
        private final TextView txtSpecialization;
        private final Button btnEditLecturer;
        private final Button btnDeleteLecturer;

        public LecturerViewHolder(View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtSpecialization = itemView.findViewById(R.id.txtSpecialization);
            btnEditLecturer = itemView.findViewById(R.id.btnEditLecturer);
            btnDeleteLecturer = itemView.findViewById(R.id.btnDeleteLecturer);
        }
    }
}
