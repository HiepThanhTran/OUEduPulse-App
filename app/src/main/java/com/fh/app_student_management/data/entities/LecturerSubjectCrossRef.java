package com.fh.app_student_management.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "lecturer_subject_cross_ref", foreignKeys = {
        @ForeignKey(
                entity = Lecturer.class,
                parentColumns = "id",
                childColumns = "lecturer_id"
        ),
        @ForeignKey(
                entity = Subject.class,
                parentColumns = "id",
                childColumns = "subject_id"
        )
})
public class LecturerSubjectCrossRef {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "lecturer_id")
    private long lecturerId;
    @ColumnInfo(name = "subject_id")
    private long subjectId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(long lecturerId) {
        this.lecturerId = lecturerId;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }
}
