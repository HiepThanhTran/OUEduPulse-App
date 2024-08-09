package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.AcademicYear;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.entities.Student;
import com.fh.app_student_management.data.entities.User;

public class StudentWithRelations {

    @Embedded
    private Student student;
    @Relation(parentColumn = "major_id", entityColumn = "id")
    private Major major;
    @Relation(parentColumn = "academic_year_id", entityColumn = "id")
    private AcademicYear academicYear;
    @Relation(parentColumn = "class_id", entityColumn = "id")
    private Class clazz;
    @Relation(parentColumn = "user_id", entityColumn = "id")
    private User user;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public AcademicYear getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(AcademicYear academicYear) {
        this.academicYear = academicYear;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
