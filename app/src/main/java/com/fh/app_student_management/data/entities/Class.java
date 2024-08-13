package com.fh.app_student_management.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "classes",
        foreignKeys = {
                @ForeignKey(
                        entity = Major.class,
                        parentColumns = "id",
                        childColumns = "major_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = AcademicYear.class,
                        parentColumns = "id",
                        childColumns = "academic_year_id",
                        onDelete = ForeignKey.SET_NULL
                )
        })
public class Class {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    @ColumnInfo(name = "major_id")
    private Long majorId;
    @ColumnInfo(name = "academic_year_id")
    private Long academicYearId;

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

    public Long getMajorId() {
        return majorId;
    }

    public void setMajorId(Long majorId) {
        this.majorId = majorId;
    }

    public Long getAcademicYearId() {
        return academicYearId;
    }

    public void setAcademicYearId(Long academicYearId) {
        this.academicYearId = academicYearId;
    }
}
