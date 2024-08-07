package com.fh.app_student_management.data.relations;

public class StudentWithScores {

    private long studentId;
    private String studentName;
    private float gkScore;
    private float ckScore;
    private float tbScore;

    public StudentWithScores(long studentId, String studentName, float gkScore, float ckScore, float tbScore) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.gkScore = gkScore;
        this.ckScore = ckScore;
        this.tbScore = tbScore;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public float getGkScore() {
        return gkScore;
    }

    public void setGkScore(float gkScore) {
        this.gkScore = gkScore;
    }

    public float getCkScore() {
        return ckScore;
    }

    public void setCkScore(float ckScore) {
        this.ckScore = ckScore;
    }

    public float getTbScore() {
        return tbScore;
    }

    public void setTbScore(float tbScore) {
        this.tbScore = tbScore;
    }
}

