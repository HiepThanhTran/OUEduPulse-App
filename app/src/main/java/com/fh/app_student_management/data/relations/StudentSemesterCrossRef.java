package com.fh.app_student_management.data.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"student_id", "semester_id"})
public class StudentSemesterCrossRef {

    @ColumnInfo(name = "student_id")
    private long studentId;
    @ColumnInfo(name = "semester_id")
    private long semesterId;
}
