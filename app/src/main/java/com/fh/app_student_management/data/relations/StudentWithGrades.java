package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Grade;
import com.fh.app_student_management.data.entities.Student;

import java.util.List;

public class StudentWithGrades {

    @Embedded
    private Student student;
    @Relation(parentColumn = "id", entityColumn = "student_id")
    private List<Grade> grades;
}
