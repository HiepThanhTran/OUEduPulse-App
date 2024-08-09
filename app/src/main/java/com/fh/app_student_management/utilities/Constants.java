package com.fh.app_student_management.utilities;

public final class Constants {

    public static final String DATABASE_NAME = "student_management.db";
    public static final int DATABASE_VERSION = 1;

    public static final String PREFS_NAME = "storage";
    public static final String PREF_NOTIFICATION_SWITCH = "notification_switch";

    public static final String USER_ID = "userId";
    public static final String LECTURER_ID = "lecturerId";
    public static final String STUDENT_ID = "studentId";
    public static final String SEMESTER_ID = "semesterId";
    public static final String CLASS_ID = "classId";
    public static final String SUBJECT_ID = "subjectId";

    public static final boolean MALE = false;
    public static final boolean FEMALE = true;

    public enum Role {
        ADMIN, LECTURER, STUDENT
    }
}
