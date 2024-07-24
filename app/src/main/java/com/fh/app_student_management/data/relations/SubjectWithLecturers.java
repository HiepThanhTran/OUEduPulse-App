package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.Subject;

import java.util.List;

public class SubjectWithLecturers {

    @Embedded
    private Subject subject;
    @Relation(parentColumn = "id", entityColumn = "id",
            associateBy = @Junction(
                    value = LecturerSubjectCrossRef.class,
                    parentColumn = "subject_id",
                    entityColumn = "lecturer_id"
            )
    )
    private List<Lecturer> lecturers;
}
