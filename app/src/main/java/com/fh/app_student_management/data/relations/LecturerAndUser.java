package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.User;

public class LecturerAndUser {

    @Embedded
    private Lecturer lecturer;
    @Relation(parentColumn = "user_id", entityColumn = "id")
    private User user;

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
