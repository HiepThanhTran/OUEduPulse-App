package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Subject;

import java.util.List;

public class ClassWithSubjects {

    @Embedded
    private Class aClass;
    @Relation(parentColumn = "id", entityColumn = "class_id")
    private List<Subject> subjects;
}
