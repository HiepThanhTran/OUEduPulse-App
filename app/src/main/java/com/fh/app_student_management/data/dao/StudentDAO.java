package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Student;

import java.util.List;

@Dao
public interface StudentDAO {

    @Query("SELECT * FROM students")
    List<Student> getAll();

    @Query("SELECT * FROM students WHERE id = :id")
    Student getById(Long id);

    @Query("SELECT * FROM students WHERE user_id = :userId")
    Student getByUserId(Long userId);

    @Insert
    Long insert(Student student);

    @Insert
    void insertAll(Student... students);

    @Update
    void update(Student student);

    @Delete
    void delete(Student student);
}
