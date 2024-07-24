package com.fh.app_student_management.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "students")
public class Student extends User {

    private String specialization;
    @ColumnInfo(name = "major_id")
    private long majorId;
    @ColumnInfo(name = "academic_year_id")
    private long academicYearId;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
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
}
