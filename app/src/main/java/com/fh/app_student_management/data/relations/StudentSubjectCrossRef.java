package com.fh.app_student_management.data.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.fh.app_student_management.data.entities.Student;
import com.fh.app_student_management.data.entities.Subject;

@Entity(primaryKeys = {"student_id", "subject_id"}, foreignKeys = {
        @ForeignKey(
                entity = Student.class,
                parentColumns = "id",
                childColumns = "student_id"
        ),
        @ForeignKey(
                entity = Subject.class,
                parentColumns = "id",
                childColumns = "subject_id"
        )
})
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
