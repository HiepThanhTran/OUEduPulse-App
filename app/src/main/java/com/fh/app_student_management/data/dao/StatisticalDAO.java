package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.fh.app_student_management.data.relations.ScoreDistribution;
import com.fh.app_student_management.data.relations.StatisticalOfLecturer;
import com.fh.app_student_management.data.relations.StatisticalOfSubject;

import java.util.List;

@Dao
public interface StatisticalDAO {

    @Query("SELECT " +
            "   l.id AS lecturerId," +
            "   u.full_name AS lecturerName," +
            "   subj.name AS subjectName," +
            "   c.name AS className " +
            "FROM lecturers l " +
            "JOIN users u ON l.user_id = u.id " +
            "JOIN lecturer_subject_cross_ref lsc ON l.id = lsc.lecturer_id " +
            "JOIN subjects subj ON lsc.subject_id = subj.id " +
            "JOIN classes c ON subj.class_id = c.id " +
            "JOIN subject_semester_cross_ref ssc ON subj.id = ssc.subject_id " +
            "JOIN semesters s ON ssc.semester_id = s.id " +
            "WHERE s.id = :semesterId AND l.id = :lecturerId " +
            "ORDER BY subj.name, c.name")
    List<StatisticalOfLecturer> getStatisticalOfLecturer(long semesterId, long lecturerId);

    @Query("SELECT " +
            "s.id AS subjectId," +
            "s.name AS subjectName," +
            "s.credits AS subjectCredits," +
            "c.name AS className," +
            "COUNT(student_subject_cross_ref.student_id) AS numberOfStudents " +
            "FROM subjects s " +
            "JOIN classes c ON s.class_id = c.id " +
            "JOIN lecturer_subject_cross_ref ON s.id = lecturer_subject_cross_ref.subject_id " +
            "JOIN subject_semester_cross_ref ON s.id = subject_semester_cross_ref.subject_id " +
            "JOIN student_subject_cross_ref ON s.id = student_subject_cross_ref.subject_id " +
            "JOIN student_semester_cross_ref ON student_subject_cross_ref.student_id = student_semester_cross_ref.student_id " +
            "WHERE subject_semester_cross_ref.semester_id = :semesterId AND student_semester_cross_ref.semester_id = :semesterId " +
            "GROUP BY s.id " +
            "ORDER BY s.name, c.name")
    List<StatisticalOfSubject> getStatisticalOfSubject(long semesterId);

    @Query("SELECT " +
            "COUNT(CASE WHEN point >= 9 THEN 1 END) AS excellent, " +
            "COUNT(CASE WHEN point >= 8.5 AND point < 9 THEN 1 END) AS good, " +
            "COUNT(CASE WHEN point >= 6.5 AND point < 8.5 THEN 1 END) AS fair, " +
            "COUNT(CASE WHEN point < 6.5 THEN 1 END) AS average " +
            "FROM scores " +
            "WHERE type = 'TB' AND semester_id = :semesterId AND subject_id = :subjectId")
    ScoreDistribution getStatisticalBySemesterSubject(long semesterId, long subjectId);
}
