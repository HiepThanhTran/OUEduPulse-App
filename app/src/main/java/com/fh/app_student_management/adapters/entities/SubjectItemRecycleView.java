package com.fh.app_student_management.adapters.entities;

import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.entities.Subject;

public class SubjectItemRecycleView {

    private Subject subject;
    private Major major;
    private Class clazz;

    public SubjectItemRecycleView(Subject subject, Major major, Class clazz) {
        this.subject = subject;
        this.major = major;
        this.clazz = clazz;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
