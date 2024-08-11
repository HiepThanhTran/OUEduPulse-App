package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Major;

import java.util.List;

@Dao
public interface MajorDAO {

    @Query("SELECT * FROM majors ORDER BY id DESC")
    List<Major> getAll();

    @Query("SELECT * FROM majors WHERE id = :id")
    Major getById(long id);

    @Query("SELECT COUNT(*) FROM majors")
    int count();

    @Insert
    long insert(Major major);

    @Insert
    void insertAll(Major... majors);

    @Update
    void update(Major Major);

    @Delete
    void delete(Major Major);
}
