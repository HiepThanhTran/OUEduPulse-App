package com.fh.app_student_management.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "students",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Major.class,
                        parentColumns = "id",
                        childColumns = "major_id",
                        onDelete = ForeignKey.SET_NULL
                ),
                @ForeignKey(
                        entity = AcademicYear.class,
                        parentColumns = "id",
                        childColumns = "academic_year_id",
                        onDelete = ForeignKey.SET_NULL
                ),
                @ForeignKey(
                        entity = Class.class,
                        parentColumns = "id",
                        childColumns = "class_id",
                        onDelete = ForeignKey.SET_NULL
                )
        })
public class Student {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(defaultValue = "0")
    private float gpa;
    @ColumnInfo(defaultValue = "0")
    private float totalCredits;
    @ColumnInfo(name = "user_id")
    private long userId;
    @ColumnInfo(name = "major_id")
    private Long majorId;
    @ColumnInfo(name = "class_id")
    private Long classId;
    @ColumnInfo(name = "academic_year_id")
    private Long academicYearId;

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getMajorId() {
        return majorId;
    }

    public void setMajorId(Long majorId) {
        this.majorId = majorId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getAcademicYearId() {
        return academicYearId;
    }

    public void setAcademicYearId(Long academicYearId) {
        this.academicYearId = academicYearId;
    }
}
