package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.data.entities.StudentSemesterCrossRef;

import java.util.List;

@Dao
public interface SemesterDAO {

    @Query("SELECT * FROM semesters")
    List<Semester> getAll();

    @Query("SELECT * FROM semesters WHERE id = :id")
    Semester getById(Long id);

    @Insert
    Long insert(Semester semester);

    @Insert
    void insertAll(Semester... semesters);

    @Update
    void update(Semester semester);

    @Delete
    void delete(Semester semester);
}
