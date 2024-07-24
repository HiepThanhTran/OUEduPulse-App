package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Grade;

import java.util.List;

@Dao
public interface GradeDAO {

    @Query("SELECT * FROM grades")
    List<Grade> getAll();

    @Query("SELECT * FROM grades WHERE id = :id")
    Grade getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Grade Grade);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Grade... grades);

    @Update
    void update(Grade Grade);

    @Delete
    void delete(Grade Grade);
}
