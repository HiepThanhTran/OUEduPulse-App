package com.fh.app_student_management.data.relations;

public class StatisticalOfLecturer {

    private long lecturerId;
    private String lecturerName;
    private String subjectName;
    private String className;
    private String semesterName;

    public StatisticalOfLecturer() {
    }

    public StatisticalOfLecturer(long lecturerId, String lecturerName, String subjectName, String className, String semesterName) {
        this.lecturerId = lecturerId;
        this.lecturerName = lecturerName;
        this.subjectName = subjectName;
        this.className = className;
        this.semesterName = semesterName;
    }

    public long getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(long lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }
}