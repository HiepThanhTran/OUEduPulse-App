package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Semester;

import java.util.List;

@Dao
public interface SemesterDAO {

    @Query("SELECT * FROM semesters")
    List<Semester> getAll();

    @Query("SELECT * FROM semesters WHERE id = :id")
    Semester getById(long id);

    @Query("SELECT * FROM semesters WHERE name LIKE '%' || :name || '%'")
    List<Semester> findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Semester semester);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Semester... semesters);

    @Update
    void update(Semester semester);

    @Delete
    void delete(Semester semester);
}
