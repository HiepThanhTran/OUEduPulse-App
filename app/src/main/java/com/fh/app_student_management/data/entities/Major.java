package com.fh.app_student_management.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "majors",
        foreignKeys = {
                @ForeignKey(
                        entity = Faculty.class,
                        parentColumns = "id",
                        childColumns = "faculty_id",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class Major {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    @ColumnInfo(name = "faculty_id")
    private long facultyId;

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

    public long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(long facultyId) {
        this.facultyId = facultyId;
    }
}
