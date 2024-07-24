package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.Subject;

import java.util.List;

public class LecturerWithSubjects {

    @Embedded
    private Lecturer lecturer;
    @Relation(parentColumn = "id", entityColumn = "id",
            associateBy = @Junction(
                    value = LecturerSubjectCrossRef.class,
                    parentColumn = "lecturer_id",
                    entityColumn = "subject_id"
            )
    )
    private List<Subject> subjects;
}
