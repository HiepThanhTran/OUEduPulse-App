package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Faculty;
import com.fh.app_student_management.data.entities.Major;

import java.util.List;

public class FacultyWithMajors {

    @Embedded
    private Faculty faculty;
    @Relation(parentColumn = "id", entityColumn = "faculty_id")
    private List<Major> majors;
}
