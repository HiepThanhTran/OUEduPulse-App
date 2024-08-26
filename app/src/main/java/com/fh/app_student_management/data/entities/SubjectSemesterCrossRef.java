package com.fh.app_student_management.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "subject_semester_cross_ref",
        foreignKeys = {
                @ForeignKey(
                        entity = Subject.class,
                        parentColumns = "id",
                        childColumns = "subject_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Semester.class,
                        parentColumns = "id",
                        childColumns = "semester_id",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class SubjectSemesterCrossRef {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "subject_id")
    private long subjectId;
    @ColumnInfo(name = "semester_id")
    private long semesterId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(long semesterId) {
        this.semesterId = semesterId;
    }
}
