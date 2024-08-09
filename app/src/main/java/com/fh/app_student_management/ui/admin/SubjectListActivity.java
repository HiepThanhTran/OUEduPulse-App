package com.fh.app_student_management.ui.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.admin.SubjectRecycleViewAdapter;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.entities.Subject;
import com.fh.app_student_management.data.relations.SubjectWithRelations;
import com.fh.app_student_management.utilities.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class SubjectListActivity extends AppCompatActivity {

    private Class selectedClass;
    private ArrayList<Class> classes;
    private String[] classNames;
    private Major selectedMajor;
    private ArrayList<Major> majors;
    private String[] majorNames;

    private SearchView searchViewSubject;
    private BottomSheetDialog bottomSheetDialog;
    private SubjectRecycleViewAdapter subjectRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_subject);

        initSubjectListView();
        handleEventListener();
    }

    private void initSubjectListView() {
        AppDatabase db = AppDatabase.getInstance(this);
        searchViewSubject = findViewById(R.id.searchViewSubject);
        bottomSheetDialog = new BottomSheetDialog(this);

        classes = new ArrayList<>(db.classDAO().getAll());
        classNames = new String[classes.size()];
        for (int i = 0; i < classes.size(); i++) {
            classNames[i] = classes.get(i).getName();
        }

        majors = new ArrayList<>(db.majorDAO().getAll());
        majorNames = new String[majors.size()];
        for (int i = 0; i < majors.size(); i++) {
            majorNames[i] = majors.get(i).getName();
        }

        RecyclerView rvSubject = findViewById(R.id.rvSubject);
        ArrayList<SubjectWithRelations> subjects = new ArrayList<>(db.subjectDAO().getAllWithRelations());
        subjectRecycleViewAdapter = new SubjectRecycleViewAdapter(this, subjects);
        rvSubject.setLayoutManager(new LinearLayoutManager(this));
        rvSubject.setAdapter(subjectRecycleViewAdapter);
    }

    @SuppressLint("InflateParams")
    private void handleEventListener() {
        findViewById(R.id.layoutSubject).setOnClickListener(v -> {
            if (v.getId() == R.id.layoutSubject) {
                searchViewSubject.clearFocus();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnAddSubject).setOnClickListener(v -> showAddSubjectDialog());

        searchViewSubject.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                subjectRecycleViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subjectRecycleViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @SuppressLint("InflateParams")
    private void showAddSubjectDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.admin_bottom_sheet_add_subject, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setSkipCollapsed(true);
        bottomSheetDialog.show();

        view.findViewById(R.id.edtClassName).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn lớp")
                .setItems(classNames, (dialog, which) -> {
                    selectedClass = classes.get(which);
                    ((EditText) view.findViewById(R.id.edtClassName)).setText(classNames[which]);
                })
                .show());

        view.findViewById(R.id.edtMajorName).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn ngành")
                .setItems(majorNames, (dialog, which) -> {
                    selectedMajor = majors.get(which);
                    ((EditText) view.findViewById(R.id.edtMajorName)).setText(majorNames[which]);
                })
                .show());

        view.findViewById(R.id.btnAddSubject).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Thêm môn học mới?")
                .setPositiveButton("Có", (dialog, which) -> performAddSubject(view))
                .setNegativeButton("Không", null)
                .show());
    }

    private void performAddSubject(View view) {
        if (!validateInputs(view)) return;

        SubjectWithRelations subjectWithRelations = new SubjectWithRelations();
        subjectWithRelations.setSubject(new Subject());

        subjectWithRelations.getSubject().setName(((EditText) view.findViewById(R.id.edtSubjectName)).getText().toString());
        subjectWithRelations.getSubject().setCredits(Integer.parseInt(((EditText) view.findViewById(R.id.edtSubjectCredits)).getText().toString()));
        subjectWithRelations.getSubject().setClassId(selectedClass.getId());
        subjectWithRelations.getSubject().setMajorId(selectedMajor.getId());
        subjectWithRelations.setClazz(selectedClass);
        subjectWithRelations.setMajor(selectedMajor);

        subjectRecycleViewAdapter.addSubject(subjectWithRelations);
        bottomSheetDialog.dismiss();
        Utils.showToast(this, "Thêm thành công");
    }

    private boolean validateInputs(View view) {
        return validateNotEmpty(view, R.id.edtSubjectName, "Tên môn không được để trống")
                && validateNotEmpty(view, R.id.edtSubjectCredits, "Số tín chỉ không được để trống")
                && validateNotEmpty(view, R.id.edtClassName, "Lớp không được để trống")
                && validateNotEmpty(view, R.id.edtMajorName, "Ngành không được để trống");
    }

    private boolean validateNotEmpty(View view, int viewId, String errorMessage) {
        EditText editText = view.findViewById(viewId);
        if (editText == null || editText.getText().toString().trim().isEmpty()) {
            Utils.showToast(this, errorMessage);
            return false;
        }
        return true;
    }
}