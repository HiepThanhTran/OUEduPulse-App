package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Lecturer;

import java.util.List;

public class LecturerWithClasses {

    @Embedded
    private Lecturer lecturer;
    @Relation(parentColumn = "id", entityColumn = "lecturer_id")
    private List<Class> classes;
}
