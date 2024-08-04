package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Class;

import java.util.List;

@Dao
public interface ClassDAO {

    @Query("SELECT * FROM classes")
    List<Class> getAll();

    @Query("SELECT * FROM classes WHERE id = :id")
    Class getById(Long id);

    @Query("SELECT * FROM classes WHERE name LIKE '%' || :name || '%'")
    List<Class> findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Class Class);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Class... classes);

    @Update
    void update(Class Class);

    @Delete
    void delete(Class Class);
}
