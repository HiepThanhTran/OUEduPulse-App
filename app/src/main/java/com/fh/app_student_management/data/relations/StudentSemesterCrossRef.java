package com.fh.app_student_management.data.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.data.entities.Student;

@Entity(primaryKeys = {"student_id", "semester_id"}, foreignKeys = {
        @ForeignKey(
                entity = Student.class,
                parentColumns = "id",
                childColumns = "student_id"
        ),
        @ForeignKey(
                entity = Semester.class,
                parentColumns = "id",
                childColumns = "semester_id"
        )
})
public class StudentSemesterCrossRef {

    @ColumnInfo(name = "student_id")
    private long studentId;
    @ColumnInfo(name = "semester_id")
    private long semesterId;

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(long semesterId) {
        this.semesterId = semesterId;
    }
}
