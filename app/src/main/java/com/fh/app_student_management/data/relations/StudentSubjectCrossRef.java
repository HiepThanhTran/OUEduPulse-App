package com.fh.app_student_management.data.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"student_id", "subject_id"})
public class StudentSubjectCrossRef {

    @ColumnInfo(name = "student_id")
    private long studentId;
    @ColumnInfo(name = "subject_id")
    private long subjectId;

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }
}
