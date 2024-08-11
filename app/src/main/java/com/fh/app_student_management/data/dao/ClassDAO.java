package com.fh.app_student_management.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.relations.ClassWithRelations;

import java.util.List;

@Dao
public interface ClassDAO {

    @Query("SELECT * FROM classes ORDER BY id DESC")
    List<Class> getAll();

    @Query("SELECT * FROM classes ORDER BY id DESC")
    List<ClassWithRelations> getAllWithRelations();

    @Query("SELECT * FROM classes WHERE id = :id")
    Class getById(long id);

    @Query("SELECT * FROM classes c " +
            "JOIN majors m ON m.id = c.major_id " +
            "JOIN faculties f ON f.id = m.faculty_id " +
            "WHERE f.id = :facultyId " +
            "ORDER BY c.id DESC")
    List<ClassWithRelations> getByFaculty(long facultyId);

    @Query("SELECT * FROM classes " +
            "WHERE major_id = :majorId " +
            "ORDER BY id DESC")
    List<ClassWithRelations> getByMajor(long majorId);

    @Query("SELECT * FROM classes " +
            "WHERE academic_year_id = :academicYearId " +
            "ORDER BY id DESC")
    List<ClassWithRelations> getByAcademicYear(long academicYearId);

    @Query("SELECT * FROM classes c " +
            "JOIN majors m ON m.id = c.major_id " +
            "JOIN faculties f ON f.id = m.faculty_id " +
            "WHERE f.id = :facultyId AND m.id = :majorId " +
            "ORDER BY c.id DESC")
    List<ClassWithRelations> getByFacultyMajor(long facultyId, long majorId);

    @Query("SELECT * FROM classes c " +
            "JOIN majors m ON m.id = c.major_id " +
            "JOIN faculties f ON f.id = m.faculty_id " +
            "WHERE f.id = :facultyId AND c.academic_year_id = :academicYearId " +
            "ORDER BY c.id DESC")
    List<ClassWithRelations> getByFacultyAcademicYear(long facultyId, long academicYearId);

    @Query("SELECT * FROM classes " +
            "WHERE major_id = :majorId AND academic_year_id = :academicYearId " +
            "ORDER BY id DESC")
    List<ClassWithRelations> getByMajorAcademicYear(long majorId, long academicYearId);

    @Query("SELECT * FROM classes c " +
            "JOIN majors m ON m.id = c.major_id " +
            "JOIN faculties f ON f.id = m.faculty_id " +
            "WHERE f.id = :facultyId AND m.id = :majorId AND c.academic_year_id = :academicYearId " +
            "ORDER BY c.id DESC")
    List<ClassWithRelations> getByFacultyMajorAcademicYear(long facultyId, long majorId, long academicYearId);

    @Query("SELECT COUNT(*) FROM classes")
    int count();

    @Insert
    long insert(Class Class);

    @Insert
    void insertAll(Class... classes);

    @Update
    void update(Class Class);

    @Delete
    void delete(Class Class);
}
