package com.fh.app_student_management.data.relations;

public class StatisticalOfSubject {

    private long subjectId;
    private String subjectName;
    private int subjectCredits;
    private String className;
    private int numberOfStudents;

    public StatisticalOfSubject() {
    }

    public StatisticalOfSubject(long subjectId, String subjectName, int subjectCredits, String className, int numberOfStudents) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCredits = subjectCredits;
        this.className = className;
        this.numberOfStudents = numberOfStudents;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectCredits() {
        return subjectCredits;
    }

    public void setSubjectCredits(int subjectCredits) {
        this.subjectCredits = subjectCredits;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }
}