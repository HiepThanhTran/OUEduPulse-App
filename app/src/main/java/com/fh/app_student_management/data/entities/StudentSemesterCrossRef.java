package com.fh.app_student_management.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_semester_cross_ref", foreignKeys = {
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

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "student_id")
    private long studentId;
    @ColumnInfo(name = "semester_id")
    private long semesterId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
