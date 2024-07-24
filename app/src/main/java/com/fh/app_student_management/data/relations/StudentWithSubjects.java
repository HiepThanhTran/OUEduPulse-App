package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Student;
import com.fh.app_student_management.data.entities.Subject;

import java.util.List;

public class StudentWithSubjects {

    @Embedded
    private Student student;
    @Relation(parentColumn = "id", entityColumn = "id",
            associateBy = @Junction(
                    value = StudentSubjectCrossRef.class,
                    parentColumn = "student_id",
                    entityColumn = "subject_id"
            )
    )
    private List<Subject> subjects;
}
