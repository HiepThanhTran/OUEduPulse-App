package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Subject;

import java.util.List;

@Dao
public interface SubjectDAO {

    @Query("SELECT * FROM subjects")
    List<Subject> getAll();

    @Query("SELECT * FROM subjects WHERE id = :id")
    Subject getById(Long id);

    @Query("SELECT * FROM subjects WHERE name LIKE '%' || :name || '%'")
    List<Subject> findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Subject subject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Subject... subjects);

    @Update
    void update(Subject subject);

    @Delete
    void delete(Subject subject);
}
