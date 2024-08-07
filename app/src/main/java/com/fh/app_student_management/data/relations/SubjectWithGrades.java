package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Score;
import com.fh.app_student_management.data.entities.Subject;

import java.util.List;

public class SubjectWithGrades {

    @Embedded
    private Subject subject;
    @Relation(parentColumn = "id", entityColumn = "subject_id")
    private List<Score> scores;
}
