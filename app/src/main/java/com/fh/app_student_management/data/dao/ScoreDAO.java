package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Score;
import com.fh.app_student_management.data.relations.ScoreDistribution;

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
            "AND semester_id = :semesterId " +
            "ORDER BY id DESC")
    List<Score> getByStudent(long semesterId, long subjectId, long studentId);

    @Query("SELECT " +
            "COUNT(CASE WHEN point >= 9 THEN 1 END) AS excellent, " +
            "COUNT(CASE WHEN point >= 7 AND point < 9 THEN 1 END) AS good, " +
            "COUNT(CASE WHEN point >= 5 AND point < 7 THEN 1 END) AS fair, " +
            "COUNT(CASE WHEN point < 5 THEN 1 END) AS average " +
            "FROM scores " +
            "WHERE type = 'TB' AND semester_id = :semesterId AND subject_id = :subjectId")
    ScoreDistribution getStatisticalBySemesterSubject(long semesterId, long subjectId);

    @Query("SELECT COUNT(*) FROM scores")
    int count();

    @Insert
    long insert(Score score);

    @Insert
    void insertAll(Score... scores);

    @Update
    void update(Score Score);

    @Delete
    void delete(Score Score);
}
