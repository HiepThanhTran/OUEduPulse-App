package com.fh.app_student_management.data.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.fh.app_student_management.data.entities.Student;
import com.fh.app_student_management.data.entities.User;

public class UserAndStudent {

    @Embedded
    User user;
    @Relation(parentColumn = "id", entityColumn = "user_id")
    Student student;
}
