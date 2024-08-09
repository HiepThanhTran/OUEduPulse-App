package com.fh.app_student_management.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "students", foreignKeys = {
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
    @ColumnInfo(name = "major_id")
    private long majorId;
    @ColumnInfo(name = "academic_year_id")
    private long academicYearId;
    @ColumnInfo(name = "class_id")
    private Long classId;
    @ColumnInfo(name = "user_id")
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMajorId() {
        return majorId;
    }

    public void setMajorId(long majorId) {
        this.majorId = majorId;
    }

    public long getAcademicYearId() {
        return academicYearId;
    }

    public void setAcademicYearId(long academicYearId) {
        this.academicYearId = academicYearId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
