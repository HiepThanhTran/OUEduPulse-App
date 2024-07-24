package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Student;

import java.util.List;

public class ClassWithStudents {

    @Embedded
    private Class aClass;
    @Relation(parentColumn = "id", entityColumn = "id",
            associateBy = @Junction(
                    value = StudentClassCrossRef.class,
                    parentColumn = "class_id",
                    entityColumn = "student_id"
            )
    )
    private List<Student> students;
}
