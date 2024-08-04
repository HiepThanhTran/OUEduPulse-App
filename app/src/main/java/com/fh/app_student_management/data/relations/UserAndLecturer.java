package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.User;

public class UserAndLecturer {

    @Embedded
    User user;
    @Relation(parentColumn = "id", entityColumn = "user_id")
    Lecturer lecturer;
}
