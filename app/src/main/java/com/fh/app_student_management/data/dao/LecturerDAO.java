package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Lecturer;

import java.util.List;

@Dao
public interface LecturerDAO {

    @Query("SELECT * FROM lecturers")
    List<Lecturer> getAll();

    @Query("SELECT * FROM lecturers WHERE id = :id")
    Lecturer getById(long id);

    @Query("SELECT * FROM lecturers WHERE full_name LIKE '%' || :name || '%'")
    List<Lecturer> findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Lecturer Lecturer);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Lecturer... lecturers);

    @Update
    void update(Lecturer Lecturer);

    @Delete
    void delete(Lecturer Lecturer);
}
