package com.fh.app_student_management.utilities;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public final class Constants {

    public static final String DATABASE_NAME = "student_management.db";
    public static final int DATABASE_VERSION = 1;

    public static final String PREFS_NAME = "storage";
    public static final String PREF_NOTIFICATION_SWITCH = "notification_switch";

    public static final String USER_EMAIL = "userEmail";
    public static final boolean MALE = false;
    public static final boolean FEMALE = true;

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public enum Role {
        ADMIN, LECTURER, STUDENT
    }
}
