package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Score;

import java.util.List;

@Dao
public interface ScoreDAO {

    @Query("SELECT * FROM scores ORDER BY id DESC")
    List<Score> getAll();

    @Query("SELECT * FROM scores WHERE id = :id")
    Score getById(long id);

    @Query("SELECT * FROM scores " +
            "WHERE student_id = :studentId " +
            "AND subject_id = :subjectId " +
            "AND semester_id = :semesterId")
    List<Score> getByStudent(long semesterId, long subjectId, long studentId);

    @Query("SELECT COUNT(*) FROM scores")
    int count();

    @Insert
    long insert(Score score);

    @Insert
    void insert(Score... scores);

    @Update
    void update(Score Score);

    @Delete
    void delete(Score Score);
}
