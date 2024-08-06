package com.fh.app_student_management.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "classes", foreignKeys = {
        @ForeignKey(
                entity = Major.class,
                parentColumns = "id",
                childColumns = "major_id"
        ),
        @ForeignKey(
                entity = AcademicYear.class,
                parentColumns = "id",
                childColumns = "academic_year_id"
        ),
        @ForeignKey(
                entity = Lecturer.class,
                parentColumns = "id",
                childColumns = "lecturer_id"
        ),
        @ForeignKey(
                entity = Semester.class,
                parentColumns = "id",
                childColumns = "semester_id"
        )
})
public class Class {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    @ColumnInfo(name = "major_id")
    private long majorId;
    @ColumnInfo(name = "academic_year_id")
    private long academicYearId;
    @ColumnInfo(name = "semester_id")
    private long semesterId;
    @ColumnInfo(name = "lecturer_id")
    private long lecturerId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(long semesterId) {
        this.semesterId = semesterId;
    }

    public long getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(long lecturerId) {
        this.lecturerId = lecturerId;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Class aClass = (Class) o;
        return id == aClass.id && Objects.equals(name, aClass.name);
    }
}
