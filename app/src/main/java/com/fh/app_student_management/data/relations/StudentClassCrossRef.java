package com.fh.app_student_management.data.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Student;

@Entity(primaryKeys = {"student_id", "class_id"}, foreignKeys = {
        @ForeignKey(
                entity = Student.class,
                parentColumns = "id",
                childColumns = "student_id"
        ),
        @ForeignKey(
                entity = Class.class,
                parentColumns = "id",
                childColumns = "class_id"
        )
})
public class StudentClassCrossRef {

    @ColumnInfo(name = "student_id")
    private long studentId;
    @ColumnInfo(name = "class_id")
    private long classId;

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }
}
