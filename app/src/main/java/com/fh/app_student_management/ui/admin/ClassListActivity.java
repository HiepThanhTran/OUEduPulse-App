package com.fh.app_student_management.ui.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fh.app_student_management.R;
import com.fh.app_student_management.adapters.admin.ClassRecycleViewAdapter;
import com.fh.app_student_management.data.AppDatabase;
import com.fh.app_student_management.data.entities.AcademicYear;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.relations.ClassWithRelations;
import com.fh.app_student_management.utilities.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ClassListActivity extends AppCompatActivity {

    private Major selectedMajor;
    private ArrayList<Major> majors;
    private String[] majorNames;
    private AcademicYear selectedAcademicYear;
    private ArrayList<AcademicYear> academicYears;
    private String[] academicYearNames;
    private ClassRecycleViewAdapter classRecycleViewAdapter;

    private RelativeLayout layoutClass;
    private ImageView btnBack;
    private SearchView searchViewClass;
    private Button btnAddClass;

    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_list_class);

        initClassListView();
        handleEventListener();
    }

    private void initClassListView() {
        layoutClass = findViewById(R.id.layoutClass);
        btnBack = findViewById(R.id.btnBack);
        btnAddClass = findViewById(R.id.btnAddClass);
        searchViewClass = findViewById(R.id.searchViewClass);
        bottomSheetDialog = new BottomSheetDialog(this);

        AppDatabase db = AppDatabase.getInstance(this);

        majors = new ArrayList<>(db.majorDAO().getAll());
        majorNames = new String[majors.size()];
        for (int i = 0; i < majors.size(); i++) {
            majorNames[i] = majors.get(i).getName();
        }

        academicYears = new ArrayList<>(db.academicYearDAO().getAll());
        academicYearNames = new String[academicYears.size()];
        for (int i = 0; i < academicYears.size(); i++) {
            academicYearNames[i] = academicYears.get(i).getName();
        }

        ArrayList<ClassWithRelations> classes = new ArrayList<>(db.classDAO().getAllWithRelations());

        RecyclerView rvClass = findViewById(R.id.rvClass);
        classRecycleViewAdapter = new ClassRecycleViewAdapter(this, classes);
        rvClass.setLayoutManager(new LinearLayoutManager(this));
        rvClass.setAdapter(classRecycleViewAdapter);
    }

    @SuppressLint("InflateParams")
    private void handleEventListener() {
        layoutClass.setOnClickListener(v -> {
            if (v.getId() == R.id.layoutClass) {
                searchViewClass.clearFocus();
            }
        });

        btnBack.setOnClickListener(v -> finish());

        searchViewClass.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                classRecycleViewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                classRecycleViewAdapter.getFilter().filter(newText);
                return false;
            }
        });

        btnAddClass.setOnClickListener(v -> showAddClassDialog());
    }

    @SuppressLint("InflateParams")
    private void showAddClassDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.admin_bottom_sheet_add_class, null);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setSkipCollapsed(true);
        behavior.setDraggable(false);
        behavior.setHideable(true);
        bottomSheetDialog.show();

        view.findViewById(R.id.edtMajor).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn lớp")
                .setItems(majorNames, (dialog, which) -> {
                    selectedMajor = majors.get(which);
                    ((EditText) view.findViewById(R.id.edtMajor)).setText(majorNames[which]);
                })
                .show());

        view.findViewById(R.id.edtAcademicYear).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Chọn ngành")
                .setItems(academicYearNames, (dialog, which) -> {
                    selectedAcademicYear = academicYears.get(which);
                    ((EditText) view.findViewById(R.id.edtAcademicYear)).setText(academicYearNames[which]);
                })
                .show());

        view.findViewById(R.id.btnAddClass).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Thêm lớp học mới?")
                .setPositiveButton("Có", (dialog, which) -> performAddClass(view))
                .setNegativeButton("Không", null)
                .show());
    }

    private void performAddClass(View view) {
        if (!validateInputs(view)) return;

        ClassWithRelations classWithRelations = new ClassWithRelations();
        classWithRelations.setClazz(new Class());

        classWithRelations.getClazz().setName(((EditText) view.findViewById(R.id.edtClassName)).getText().toString());
        classWithRelations.getClazz().setMajorId(selectedMajor.getId());
        classWithRelations.getClazz().setAcademicYearId(selectedAcademicYear.getId());
        classWithRelations.setMajor(selectedMajor);
        classWithRelations.setAcademicYear(selectedAcademicYear);

        classRecycleViewAdapter.addClass(classWithRelations);
        bottomSheetDialog.dismiss();
        Utils.showToast(this, "Thêm thành công");
    }

    private boolean validateInputs(View view) {
        return validateNotEmpty(view, R.id.edtClassName, "Tên lớp không được để trống")
                && validateNotEmpty(view, R.id.edtMajor, "Ngành không được để trống")
                && validateNotEmpty(view, R.id.edtAcademicYear, "Năm học không được để trống");
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