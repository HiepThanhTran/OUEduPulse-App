package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.LecturerSubjectCrossRef;
import com.fh.app_student_management.data.entities.StudentSemesterCrossRef;
import com.fh.app_student_management.data.entities.StudentSubjectCrossRef;
import com.fh.app_student_management.data.entities.SubjectSemesterCrossRef;

import java.util.List;

@Dao
public interface CrossRefDAO {

    @Query("SELECT * FROM lecturer_subject_cross_ref ORDER BY id DESC")
    List<LecturerSubjectCrossRef> getAllLecturerSubjectCrossRef();

    @Query("SELECT * FROM student_semester_cross_ref ORDER BY id DESC")
    List<StudentSemesterCrossRef> getAllStudentSemesterCrossRef();

    @Query("SELECT * FROM student_subject_cross_ref ORDER BY id DESC")
    List<StudentSubjectCrossRef> getAllStudentSubjectCrossRef();

    @Query("SELECT * FROM subject_semester_cross_ref ORDER BY id DESC")
    List<SubjectSemesterCrossRef> getAllSubjectSemesterCrossRef();

    @Query("SELECT * FROM lecturer_subject_cross_ref " +
            "WHERE lecturer_id = :lecturerId AND subject_id = :subjectId")
    LecturerSubjectCrossRef getLecturerSubjectCrossRef(long lecturerId, long subjectId);

    @Query("SELECT * FROM student_semester_cross_ref " +
            "WHERE student_id = :studentId AND semester_id = :semesterId")
    StudentSemesterCrossRef getStudentSemesterCrossRef(long studentId, long semesterId);

    @Query("SELECT * FROM student_subject_cross_ref " +
            "WHERE student_id = :studentId AND subject_id = :subjectId")
    StudentSubjectCrossRef getStudentSubjectCrossRef(long studentId, long subjectId);

    @Query("SELECT * FROM subject_semester_cross_ref " +
            "WHERE subject_id = :subjectId AND semester_id = :semesterId")
    SubjectSemesterCrossRef getSubjectSemesterCrossRef(long subjectId, long semesterId);

    @Insert
    void insertLecturerSubjectCrossRef(LecturerSubjectCrossRef lecturerSubjectCrossRef);

    @Insert
    void insertStudentSemesterCrossRef(StudentSemesterCrossRef studentSemesterCrossRef);

    @Insert
    void insertStudentSubjectCrossRef(StudentSubjectCrossRef studentSubjectCrossRef);

    @Insert
    void insertSubjectSemesterCrossRef(SubjectSemesterCrossRef subjectSemesterCrossRef);

    @Update
    void updateLecturerSubjectCrossRef(LecturerSubjectCrossRef lecturerSubjectCrossRef);

    @Update
    void updateStudentSemesterCrossRef(StudentSemesterCrossRef studentSemesterCrossRef);

    @Update
    void updateStudentSubjectCrossRef(StudentSubjectCrossRef studentSubjectCrossRef);

    @Update
    void updateSubjectSemesterCrossRef(SubjectSemesterCrossRef subjectSemesterCrossRef);

    @Delete
    void deleteLecturerSubjectCrossRef(LecturerSubjectCrossRef lecturerSubjectCrossRef);

    @Delete
    void deleteStudentSemesterCrossRef(StudentSemesterCrossRef studentSemesterCrossRef);

    @Delete
    void deleteStudentSubjectCrossRef(StudentSubjectCrossRef studentSubjectCrossRef);

    @Delete
    void deleteSubjectSemesterCrossRef(SubjectSemesterCrossRef subjectSemesterCrossRef);
}
