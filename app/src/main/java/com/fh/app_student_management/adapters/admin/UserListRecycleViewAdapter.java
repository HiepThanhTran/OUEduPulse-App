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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.User;
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

public class UserListRecycleViewAdapter extends RecyclerView.Adapter<UserListRecycleViewAdapter.LecturerViewHolder> implements Filterable {

    private final Context context;
    private final ArrayList<User> originalList;
    private final BottomSheetDialog bottomSheetDialog;

    private String currentFilterText = "";
    private Constants.Role selectedRole;
    private ArrayList<User> filteredList;

    public UserListRecycleViewAdapter(Context context, ArrayList<User> originalList) {
        this.context = context;
        this.originalList = originalList;
        this.filteredList = originalList;

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
        User user = filteredList.get(position);
        holder.avatar.setImageBitmap(Utils.getBitmapFromBytes(user.getAvatar()));
        holder.txtUsername.setText(user.getFullName());
        holder.txtRoleName.setText(Utils.getRoleName(user.getRole()));

        holder.btnEditUser.setOnClickListener(v -> showEditLecturerDialog(user));

        holder.btnDeleteUser.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Xác nhận xóa người dùng?")
                .setPositiveButton("Có", (dialog, which) -> deleteUser(user))
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
                    ArrayList<User> filtered = new ArrayList<>();
                    for (User user : originalList) {
                        String lecturerName = Utils.removeVietnameseAccents(user.getFullName().toLowerCase(Locale.getDefault()));

                        if (lecturerName.contains(query)) {
                            filtered.add(user);
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
                filteredList = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
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

    public void addUser(LecturerAndUser lecturerAndUser) {
        long userId;
        try {
            userId = AppDatabase.getInstance(context).userDAO().insert(lecturerAndUser.getUser());
        } catch (SQLiteConstraintException ex) {
            Utils.showToast(context, "Email đã tồn tại!");
            return;
        }

        if (lecturerAndUser.getLecturer() != null) {
            lecturerAndUser.getLecturer().setUserId(userId);
            AppDatabase.getInstance(context).lecturerDAO().insert(lecturerAndUser.getLecturer());
        }

        originalList.add(0, lecturerAndUser.getUser());
        notifyItemInserted(0);
    }

    private void editUser(User user) {
        AppDatabase.getInstance(context).userDAO().update(user);
        getFilter().filter(currentFilterText);
        notifyItemChanged(originalList.indexOf(user));
    }

    private void deleteUser(User user) {
        if (user.getRole() == Constants.Role.LECTURER) {
            AppDatabase.getInstance(context).lecturerDAO().deleteByUser(user.getId());
        }
        AppDatabase.getInstance(context).userDAO().delete(user);
        originalList.remove(user);
        filteredList.remove(user);
        getFilter().filter(currentFilterText);
        notifyItemRemoved(originalList.indexOf(user));
    }

    @SuppressLint("InflateParams")
    private void showEditLecturerDialog(User user) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_bottom_sheet_edit_user, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setSkipCollapsed(true);
        behavior.setDraggable(false);
        bottomSheetDialog.show();

        ImageView iconCamera = view.findViewById(R.id.iconCamera);
        ImageView avatar = view.findViewById(R.id.avatar);
        EditText edtEmail = view.findViewById(R.id.edtEmail);
        EditText edtFullName = view.findViewById(R.id.edtFullName);
        EditText txtRole = view.findViewById(R.id.txtRole);
        RadioGroup radioGroupGender = view.findViewById(R.id.radioGroupGender);
        EditText edtDob = view.findViewById(R.id.edtDob);
        EditText edtAddress = view.findViewById(R.id.edtAddress);
        EditText edtSpecialization = view.findViewById(R.id.edtSpecialization);
        EditText edtDegree = view.findViewById(R.id.edtDegree);
        EditText edtCertificate = view.findViewById(R.id.edtCertificate);
        Button btnEdit = view.findViewById(R.id.btnEditUser);

        selectedRole = user.getRole();

        ((LinearLayout) edtSpecialization.getParent()).setVisibility(View.GONE);
        ((LinearLayout) edtDegree.getParent()).setVisibility(View.GONE);
        ((LinearLayout) edtCertificate.getParent()).setVisibility(View.GONE);

        avatar.setImageBitmap(Utils.getBitmapFromBytes(user.getAvatar()));
        edtEmail.setText(user.getEmail());
        edtFullName.setText(user.getFullName());
        txtRole.setText(Utils.getRoleName(user.getRole()));
        if (user.getGender() == Constants.MALE) {
            radioGroupGender.check(R.id.radioButtonMale);
        } else {
            radioGroupGender.check(R.id.radioButtonFemale);
        }
        edtDob.setText(Utils.formatDate("dd/MM/YYYY").format(user.getDob()));
        edtAddress.setText(user.getAddress());

        if (user.getRole() == Constants.Role.LECTURER) {
            Lecturer lecturer = AppDatabase.getInstance(context).lecturerDAO().getByUser(user.getId());
            edtSpecialization.setVisibility(View.VISIBLE);
            edtDegree.setVisibility(View.VISIBLE);
            edtCertificate.setVisibility(View.VISIBLE);
            edtSpecialization.setText(lecturer.getSpecialization());
            edtDegree.setText(lecturer.getDegree());
            edtCertificate.setText(lecturer.getCertificate());
        }

        iconCamera.setOnClickListener(v1 -> ImagePicker.with((Activity) context)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        String[] roleNames = {"Quản trị viên", "Chuyên viên", "Giảng viên"};
        txtRole.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Chọn vai trò")
                .setItems(roleNames, (dialog, which) -> {
                    txtRole.setText(roleNames[which]);

                    switch (which) {
                        case 0:
                            selectedRole = Constants.Role.ADMIN;
                            ((LinearLayout) edtSpecialization.getParent()).setVisibility(View.GONE);
                            ((LinearLayout) edtDegree.getParent()).setVisibility(View.GONE);
                            ((LinearLayout) edtCertificate.getParent()).setVisibility(View.GONE);
                            break;
                        case 1:
                            selectedRole = Constants.Role.SPECIALIST;
                            ((LinearLayout) edtSpecialization.getParent()).setVisibility(View.GONE);
                            ((LinearLayout) edtDegree.getParent()).setVisibility(View.GONE);
                            ((LinearLayout) edtCertificate.getParent()).setVisibility(View.GONE);
                            break;
                        case 2:
                            selectedRole = Constants.Role.LECTURER;
                            ((LinearLayout) edtSpecialization.getParent()).setVisibility(View.VISIBLE);
                            ((LinearLayout) edtDegree.getParent()).setVisibility(View.VISIBLE);
                            ((LinearLayout) edtCertificate.getParent()).setVisibility(View.VISIBLE);
                    }
                })
                .show());

        edtDob.setOnClickListener(this::showDatePickerDialog);

        btnEdit.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Thông báo")
                .setMessage("Xác nhận chỉnh sửa thông tin người dùng?")
                .setPositiveButton("Có", (dialog, which) -> performEditLecturer(user, view))
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .show());
    }

    private void performEditLecturer(User user, View view) {
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
            user.setAvatar(Utils.getBytesFromBitmap(Utils.getBitmapFromView(avatar)));
            user.setFullName(edtFullName.getText().toString());
            user.setRole(selectedRole);
            user.setDob(Utils.formatDate("dd/MM/YYYY").parse(edtDob.getText().toString()));
            user.setAddress(edtAddress.getText().toString());
            int genderId = radioGroupGender.getCheckedRadioButtonId();
            user.setGender(genderId == R.id.radioButtonMale ? Constants.MALE : Constants.FEMALE);

            if (user.getRole() == Constants.Role.LECTURER) {
                Lecturer lecturer = AppDatabase.getInstance(context).lecturerDAO().getByUser(user.getId());
                lecturer.setSpecialization(edtSpecialization.getText().toString());
                lecturer.setDegree(edtDegree.getText().toString());
                lecturer.setCertificate(edtCertificate.getText().toString());

                AppDatabase.getInstance(context).lecturerDAO().update(lecturer);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        editUser(user);
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
                && validateNotEmpty(view, R.id.txtRole, "Vai trò không được trống")
                && validateNotEmpty(view, R.id.edtDob, "Ngày sinh không được để trống")
                && validateNotEmpty(view, R.id.edtAddress, "Địa chỉ không được để trống")
                && validateNotEmpty(view, R.id.edtSpecialization, "Chuyên ngành không được để trống")
                && validateNotEmpty(view, R.id.edtDegree, "Bằng cấp không được để trống")
                && validateNotEmpty(view, R.id.edtCertificate, "Chứng chỉ không được để trống");
    }

    private boolean validateNotEmpty(View view, int viewId, String errorMessage) {
        EditText editText = view.findViewById(viewId);
        if (editText.getVisibility() == View.VISIBLE && editText.getText().toString().trim().isEmpty()) {
            Utils.showToast(context, errorMessage);
            return false;
        }
        return true;
    }

    public static class LecturerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView avatar;
        private final TextView txtUsername;
        private final TextView txtRoleName;
        private final Button btnEditUser;
        private final Button btnDeleteUser;

        public LecturerViewHolder(View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtRoleName = itemView.findViewById(R.id.txtRoleName);
            btnEditUser = itemView.findViewById(R.id.btnEditUser);
            btnDeleteUser = itemView.findViewById(R.id.btnDeleteUser);
        }
    }
}
