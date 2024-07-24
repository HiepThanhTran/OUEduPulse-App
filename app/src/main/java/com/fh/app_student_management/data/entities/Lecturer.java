package com.fh.app_student_management.data.entities;

import androidx.room.Entity;

@Entity(tableName = "lecturers")
public class Lecturer extends User {

    private String specialization;
    private String degree;
    private String certificate;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }
}
