package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.relations.LecturerAndUser;
import com.fh.app_student_management.data.relations.StatisticalOfLecturer;

import java.util.List;

@Dao
public interface LecturerDAO {

    @Query("SELECT * FROM lecturers ORDER BY id DESC")
    List<Lecturer> getAll();

    @Query("SELECT * FROM lecturers ORDER BY id DESC")
    List<LecturerAndUser> getAllLecturerAndUser();

    @Query("SELECT * FROM lecturers WHERE id = :id")
    Lecturer getById(long id);

    @Query("SELECT * FROM lecturers WHERE user_id = :userId")
    LecturerAndUser getByUser(long userId);

    @Query("SELECT COUNT(*) FROM lecturers")
    int count();

    @Insert
    long insert(Lecturer lecturer);

    @Insert
    void insertAll(Lecturer... lecturers);

    @Update
    void update(Lecturer Lecturer);

    @Delete
    void delete(Lecturer Lecturer);
}
