package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.AcademicYear;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Semester;

import java.util.List;

public class SemesterWithClasses {

    @Embedded
    private Semester semester;
    @Relation(parentColumn = "id", entityColumn = "semester_id")
    private List<Class> classes;
}
