package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.data.entities.Student;

import java.util.List;

public class SemesterWithStudents {

    @Embedded
    private Semester semester;
    @Relation(parentColumn = "id", entityColumn = "id",
            associateBy = @Junction(
                    value = StudentSemesterCrossRef.class,
                    parentColumn = "semester_id",
                    entityColumn = "student_id"
            )
    )
    private List<Student> students;
}
