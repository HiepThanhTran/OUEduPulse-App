package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.AcademicYear;
import com.fh.app_student_management.data.entities.Student;

import java.util.List;

public class AcademicYearWithStudents {

    @Embedded
    private AcademicYear academicYear;
    @Relation(parentColumn = "id", entityColumn = "academic_year_id")
    private List<Student> students;
}
