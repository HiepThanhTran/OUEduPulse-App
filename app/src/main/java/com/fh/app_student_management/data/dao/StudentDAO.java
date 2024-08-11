package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Student;
import com.fh.app_student_management.data.relations.StudentWithRelations;
import com.fh.app_student_management.data.relations.StudentWithScores;

import java.util.List;

@Dao
public interface StudentDAO {

    @Query("SELECT * FROM students ORDER BY id DESC")
    List<Student> getAll();

    @Query("SELECT * FROM students ORDER BY id DESC")
    List<StudentWithRelations> getAllWithRelations();

    @Query("SELECT * FROM students WHERE id = :id")
    Student getById(long id);

    @Query("SELECT * FROM students WHERE user_id = :userId")
    Student getByUser(long userId);

    @Query("SELECT " +
            "   s.id AS studentId, " +
            "   u.full_name AS studentName, " +
            "   COALESCE(gk.point, 0) AS gkScore, " +
            "   COALESCE(ck.point, 0) AS ckScore, " +
            "   COALESCE(tb.point, 0) AS tbScore " +
            "FROM students s " +
            "JOIN users u ON s.user_id = u.id " +
            "JOIN student_subject_cross_ref ssr ON s.id = ssr.student_id " +
            "JOIN subject_semester_cross_ref ssc ON ssr.subject_id = ssc.subject_id " +
            "LEFT JOIN ( " +
            "    SELECT student_id, subject_id, point " +
            "    FROM scores " +
            "    WHERE type = 'GK' " +
            ") gk ON s.id = gk.student_id AND ssr.subject_id = gk.subject_id " +
            "LEFT JOIN ( " +
            "    SELECT student_id, subject_id, point " +
            "    FROM scores " +
            "    WHERE type = 'CK' " +
            ") ck ON s.id = ck.student_id AND ssr.subject_id = ck.subject_id " +
            "LEFT JOIN ( " +
            "    SELECT student_id, subject_id, point " +
            "    FROM scores " +
            "    WHERE type = 'TB' " +
            ") tb ON s.id = tb.student_id AND ssr.subject_id = tb.subject_id " +
            "WHERE ssr.subject_id = :subjectId AND ssc.semester_id = :semesterId " +
            "ORDER BY studentId DESC")
    List<StudentWithScores> getScoresBySubjectSemester(long subjectId, long semesterId);

    @Query("SELECT s.* FROM students s " +
            "JOIN student_semester_cross_ref ssc ON s.id = ssc.student_id " +
            "WHERE ssc.semester_id = :semesterId " +
            "ORDER BY s.id DESC")
    List<StudentWithRelations> getBySemester(long semesterId);

    @Query("SELECT s.* FROM students s " +
            "JOIN student_semester_cross_ref sscr ON s.id = sscr.student_id " +
            "JOIN student_subject_cross_ref ssur ON s.id = ssur.student_id " +
            "JOIN subjects subj ON ssur.subject_id= subj.id " +
            "WHERE sscr.semester_id = :semesterId AND subj.class_id = :classId " +
            "ORDER BY s.id DESC")
    List<StudentWithRelations> getBySemesterClass(long semesterId, long classId);

    @Query("SELECT s.* FROM students s " +
            "JOIN student_semester_cross_ref sscr ON s.id = sscr.student_id " +
            "JOIN student_subject_cross_ref  ssur ON s.id = ssur.student_id " +
            "JOIN subjects subj ON ssur.subject_id= subj.id " +
            "WHERE sscr.semester_id = :semesterId AND subj.class_id = :classId AND subj.id = :subjectId " +
            "ORDER BY s.id DESC")
    List<StudentWithRelations> getBySemesterClassSubject( long semesterId, long classId, long subjectId);

    @Query("SELECT COUNT(*) FROM students")
    int count();

    @Insert
    long insert(Student student);

    @Insert
    void insertAll(Student... students);

    @Update
    void update(Student student);

    @Delete
    void delete(Student student);
}
