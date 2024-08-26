package com.fh.app_student_management.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_semester_cross_ref",
        foreignKeys = {
                @ForeignKey(
                        entity = Student.class,
                        parentColumns = "id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Semester.class,
                        parentColumns = "id",
                        childColumns = "semester_id",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class StudentSemesterCrossRef {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(defaultValue = "0")
    private float gpa;
    @ColumnInfo(defaultValue = "0")
    private float totalCredits;
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

    public float getGpa() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    public float getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(float totalCredits) {
        this.totalCredits = totalCredits;
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
