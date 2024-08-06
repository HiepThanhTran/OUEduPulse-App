package com.fh.app_student_management.adapters.entities;

public class SubjectItemRecycleView {

    private String subjectName;
    private int credits;

    public SubjectItemRecycleView(String subjectName, int credits) {
        this.subjectName = subjectName;
        this.credits = credits;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
