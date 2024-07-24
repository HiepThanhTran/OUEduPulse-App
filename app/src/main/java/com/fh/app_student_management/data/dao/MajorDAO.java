package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Major;

import java.util.List;

@Dao
public interface MajorDAO {

    @Query("SELECT * FROM majors")
    List<Major> getAll();

    @Query("SELECT * FROM majors WHERE id = :id")
    Major getById(long id);

    @Query("SELECT * FROM majors WHERE name LIKE '%' || :name || '%'")
    List<Major> findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Major Major);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Major... majors);

    @Update
    void update(Major Major);

    @Delete
    void delete(Major Major);
}
