package com.fh.app_student_management.data.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"lecturer_id", "subject_id"})
public class LecturerSubjectCrossRef {

    @ColumnInfo(name = "lecturer_id")
    private long lecturerId;
    @ColumnInfo(name = "subject_id")
    private long subjectId;
}
