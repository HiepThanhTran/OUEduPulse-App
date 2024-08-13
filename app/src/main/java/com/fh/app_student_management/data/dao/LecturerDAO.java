package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.data.relations.LecturerAndUser;

import java.util.List;

@Dao
public interface LecturerDAO {

    @Query("SELECT * FROM lecturers ORDER BY id DESC")
    List<Lecturer> getAll();

    @Query("SELECT l.* FROM lecturers l " +
            "JOIN users u ON u.id = l.user_id " +
            "JOIN lecturer_subject_cross_ref lsc ON l.id = lsc.lecturer_id " +
            "JOIN subject_semester_cross_ref ssc ON lsc.subject_id = ssc.subject_id " +
            "WHERE ssc.semester_id = :semesterId " +
            "GROUP BY u.full_name")
    List<LecturerAndUser> getAllLecturerAndUserBySemester(long semesterId);

    @Query("SELECT * FROM lecturers WHERE id = :id")
    Lecturer getById(long id);

    @Query("SELECT * FROM lecturers WHERE user_id = :userId")
    Lecturer getByUser(long userId);

    @Query("SELECT COUNT(*) FROM lecturers")
    int count();

    @Insert
    long insert(Lecturer lecturer);

    @Insert
    void insert(Lecturer... lecturers);

    @Update
    void update(Lecturer Lecturer);

    @Delete
    void delete(Lecturer Lecturer);

    @Query("DELETE FROM lecturers WHERE user_id = :userId")
    void deleteByUser(long userId);
}
