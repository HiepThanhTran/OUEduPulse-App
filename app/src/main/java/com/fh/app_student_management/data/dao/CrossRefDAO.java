package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

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

    @Insert
    void insertLecturerSubjectCrossRef(LecturerSubjectCrossRef... lecturerSubjectCrossRefs);

    @Insert
    void insertStudentSemesterCrossRef(StudentSemesterCrossRef... studentSemesterCrossRefs);

    @Insert
    void insertStudentSubjectCrossRef(StudentSubjectCrossRef... studentSubjectCrossRefs);

    @Insert
    void insertSubjectSemesterCrossRef(SubjectSemesterCrossRef... subjectSemesterCrossRef);
}
