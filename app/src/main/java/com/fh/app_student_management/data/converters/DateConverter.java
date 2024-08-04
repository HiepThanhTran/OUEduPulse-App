package com.fh.app_student_management.data.converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date getDateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long getTimestampFromDate(Date date) {
        return date == null ? null : date.getTime();
    }
}
