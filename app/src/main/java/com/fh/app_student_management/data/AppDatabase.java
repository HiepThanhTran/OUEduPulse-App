package com.fh.app_student_management.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.fh.app_student_management.data.converters.DateConverter;
import com.fh.app_student_management.data.dao.AcademicYearDAO;
import com.fh.app_student_management.data.dao.ClassDAO;
import com.fh.app_student_management.data.dao.FacultyDAO;
import com.fh.app_student_management.data.dao.GradeDAO;
import com.fh.app_student_management.data.dao.LecturerDAO;
import com.fh.app_student_management.data.dao.MajorDAO;
import com.fh.app_student_management.data.dao.SemesterDAO;
import com.fh.app_student_management.data.dao.StudentDAO;
import com.fh.app_student_management.data.dao.SubjectDAO;
import com.fh.app_student_management.data.dao.UserDAO;
import com.fh.app_student_management.data.entities.AcademicYear;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Faculty;
import com.fh.app_student_management.data.entities.Grade;
import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.data.entities.Student;
import com.fh.app_student_management.data.entities.Subject;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.data.relations.LecturerSubjectCrossRef;
import com.fh.app_student_management.data.relations.StudentClassCrossRef;
import com.fh.app_student_management.data.relations.StudentSemesterCrossRef;
import com.fh.app_student_management.data.relations.StudentSubjectCrossRef;
import com.fh.app_student_management.utilities.AppExecutors;
import com.fh.app_student_management.utilities.Constants;

@Database(entities = {
        AcademicYear.class,
        Class.class,
        Faculty.class,
        Lecturer.class,
        Major.class,
        Grade.class,
        Semester.class,
        Student.class,
        Subject.class,
        User.class,
        LecturerSubjectCrossRef.class,
        StudentSemesterCrossRef.class,
        StudentClassCrossRef.class,
        StudentSubjectCrossRef.class
}, version = Constants.DATABASE_VERSION, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, Constants.DATABASE_NAME).addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            AppExecutors.getInstance().diskIO().execute(() -> {
                                UserDAO userDao = INSTANCE.userDAO();
                                if (userDao.countByRole(Constants.Role.ADMIN) == 0) {
                                    User admin = new User();
                                    admin.setFullName("Administrator");
                                    admin.setEmail("admin@gmail.com");
                                    admin.setPassword("Admin@123");
                                    admin.setRole(Constants.Role.ADMIN);
                                    userDao.insert(admin);
                                }
                            });
                        }
                    }).fallbackToDestructiveMigration().allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract AcademicYearDAO academicYearDAO();

    public abstract ClassDAO classDAO();

    public abstract FacultyDAO facultyDAO();

    public abstract LecturerDAO lecturerDAO();

    public abstract MajorDAO majorDAO();

    public abstract GradeDAO pointDAO();

    public abstract SemesterDAO semesterDAO();

    public abstract StudentDAO studentDAO();

    public abstract SubjectDAO subjectDAO();

    public abstract UserDAO userDAO();
}
