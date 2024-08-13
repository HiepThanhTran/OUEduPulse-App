package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.AcademicYear;

import java.util.List;

@Dao
public interface AcademicYearDAO {

    @Query("SELECT * FROM academic_years ORDER BY id DESC")
    List<AcademicYear> getAll();

    @Query("SELECT * FROM academic_years WHERE id = :id")
    AcademicYear getById(long id);

    @Query("SELECT COUNT(*) FROM academic_years")
    int count();

    @Insert
    long insert(AcademicYear academicYear);

    @Insert
    void insert(AcademicYear... academicYears);

    @Update
    void update(AcademicYear academicYear);

    @Delete
    void delete(AcademicYear academicYear);
}
