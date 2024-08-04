package com.fh.app_student_management.utilities;

public final class Constants {

    public static final String DATABASE_NAME = "student_management.db";
    public static final int DATABASE_VERSION = 1;
    public static final String KEY_SHARED_PREFERENCES = "storage";

    public enum Role {
        ADMIN, LECTURER, STUDENT
    }
}
