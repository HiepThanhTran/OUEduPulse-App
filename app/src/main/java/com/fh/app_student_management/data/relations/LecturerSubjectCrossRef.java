package com.fh.app_student_management.data.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.Subject;

@Entity(primaryKeys = {"lecturer_id", "subject_id"}, foreignKeys = {
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

    @ColumnInfo(name = "lecturer_id")
    private long lecturerId;
    @ColumnInfo(name = "subject_id")
    private long subjectId;

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

