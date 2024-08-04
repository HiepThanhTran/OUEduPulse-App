package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Faculty;

import java.util.List;

@Dao
public interface FacultyDAO {

    @Query("SELECT * FROM faculties")
    List<Faculty> getAll();

    @Query("SELECT * FROM faculties WHERE id = :id")
    Faculty getById(Long id);

    @Query("SELECT * FROM faculties WHERE name LIKE '%' || :name || '%'")
    List<Faculty> findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Faculty Faculty);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Faculty... faculties);

    @Update
    void update(Faculty Faculty);

    @Delete
    void delete(Faculty Faculty);
}
