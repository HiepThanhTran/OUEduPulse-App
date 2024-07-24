package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.entities.Subject;

import java.util.List;

public class MajorWithSubjects {

    @Embedded
    private Major major;
    @Relation(parentColumn = "id", entityColumn = "major_id")
    private List<Subject> subjects;
}
