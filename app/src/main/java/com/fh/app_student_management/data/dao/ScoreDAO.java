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

    @Query("SELECT * FROM scores")
    List<Score> getAll();

    @Query("SELECT * FROM scores WHERE id = :id")
    Score getById(Long id);

    @Insert
    Long insert(Score score);

    @Insert
    void insertAll(Score... scores);

    @Update
    void update(Score Score);

    @Delete
    void delete(Score Score);
}
