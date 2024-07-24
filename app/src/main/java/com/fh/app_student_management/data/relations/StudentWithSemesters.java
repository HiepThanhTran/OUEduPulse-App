package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.data.entities.Student;

import java.util.List;

public class StudentWithSemesters {

    @Embedded
    private Student student;
    @Relation(parentColumn = "id", entityColumn = "id",
            associateBy = @Junction(
                    value = StudentSemesterCrossRef.class,
                    parentColumn = "student_id",
                    entityColumn = "semester_id"
            )
    )
    private List<Semester> semesters;
}
