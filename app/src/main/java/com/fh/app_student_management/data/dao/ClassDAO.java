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

    @Query("SELECT c.* FROM classes c " +
            "WHERE c.semester_id = :selectedSemesterId AND c.lecturer_id IN (" +
            "  SELECT l.id FROM lecturers l " +
            "  JOIN users u ON l.user_id = u.id " +
            "  WHERE u.id = :currentUserId)")
    List<Class> getBySemesterAndLecturer(long selectedSemesterId, long currentUserId);

    @Insert
    Long insert(Class Class);

    @Insert
    void insertAll(Class... classes);

    @Update
    void update(Class Class);

    @Delete
    void delete(Class Class);
}
