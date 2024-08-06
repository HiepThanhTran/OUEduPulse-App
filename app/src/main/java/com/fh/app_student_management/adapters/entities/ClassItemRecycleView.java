package com.fh.app_student_management.adapters.entities;

public class ClassItemRecycleView {

    private long id;
    private long semesterId;
    private String className;
    private String facultyName;
    private String majorName;
    private String academicYearName;

    public ClassItemRecycleView(long id, long semesterId, String className, String facultyName, String majorName, String academicYearName) {
        this.id = id;
        this.semesterId = semesterId;
        this.className = className;
        this.facultyName = facultyName;
        this.majorName = majorName;
        this.academicYearName = academicYearName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(long semesterId) {
        this.semesterId = semesterId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getAcademicYearName() {
        return academicYearName;
    }

    public void setAcademicYearName(String academicYearName) {
        this.academicYearName = academicYearName;
    }
}
