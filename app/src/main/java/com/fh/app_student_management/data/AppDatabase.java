package com.fh.app_student_management.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.fh.app_student_management.R;
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
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
                                    AppDatabase.class, Constants.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void insertDefaultValue(Context context) {
        insertAcademicYearList(context);
        insertSemesterList(context);
        insertFacultyList(context);
        insertMajorList(context);
        insertSubjectList(context);
        insertUser(context);
        insertClassList(context);
    }

    private static void insertAcademicYearList(Context context) {
        AcademicYearDAO academicYearDAO = getInstance(context).academicYearDAO();

        for (int i = 2021; i <= 2025; i++) {
            AcademicYear academicYear = new AcademicYear();
            academicYear.setName("Khóa " + i);
            Calendar startCal = Calendar.getInstance();
            startCal.set(i, Calendar.SEPTEMBER, 1);
            academicYear.setStartDate(startCal.getTime());

            Calendar endCal = Calendar.getInstance();
            endCal.set(i + 4, Calendar.AUGUST, 31);
            academicYear.setEndDate(endCal.getTime());

            academicYearDAO.insert(academicYear);
        }
    }

    private static void insertSemesterList(Context context) {
        AcademicYearDAO academicYearDAO = getInstance(context).academicYearDAO();
        SemesterDAO semesterDAO = getInstance(context).semesterDAO();
        List<AcademicYear> academicYears = academicYearDAO.getAll();
        Calendar calendar = Calendar.getInstance();
        int[][] semesterMonths = {
                {0, 2}, // Học kỳ 1: từ tháng 1 đến tháng 3
                {3, 5}, // Học kỳ 2: từ tháng 4 đến tháng 6
                {6, 8}  // Học kỳ 3: từ tháng 7 đến tháng 9
        };

        for (AcademicYear academicYear : academicYears) {
            long academicYearId = academicYear.getId();

            for (int i = 0; i < semesterMonths.length; i++) {
                Semester semester = new Semester();
                semester.setName("Học kỳ " + (i + 1));

                calendar.setTime(academicYear.getStartDate());
                calendar.set(Calendar.MONTH, semesterMonths[i][0]);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                semester.setStartDate(calendar.getTime());

                calendar.setTime(academicYear.getStartDate());
                calendar.set(Calendar.MONTH, semesterMonths[i][1]);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                semester.setEndDate(calendar.getTime());

                semester.setAcademicYearId(academicYearId);
                semesterDAO.insert(semester);
            }
        }
    }

    private static void insertFacultyList(Context context) {
        FacultyDAO facultyDAO = getInstance(context).facultyDAO();

        String[] faculties = {
                "Khoa học máy tính",
                "Công nghệ phần mềm",
                "Kỹ thuật máy tính",
                "Hệ thống thông tin",
                "Khoa học dữ liệu",
                "Trí tuệ nhân tạo",
                "An toàn thông tin"
        };

        for (String facultyName : faculties) {
            Faculty faculty = new Faculty();
            faculty.setName(facultyName);
            facultyDAO.insert(faculty);
        }
    }

    private static void insertMajorList(Context context) {
        FacultyDAO facultyDAO = getInstance(context).facultyDAO();
        MajorDAO majorDAO = getInstance(context).majorDAO();

        String[][] majorsByFaculty = {
                {"Công nghệ thông tin", "An toàn thông tin", "Hệ thống thông tin", "Mạng máy tính", "Lập trình di động"},
                {"Khoa học máy tính", "Trí tuệ nhân tạo", "Khoa học dữ liệu", "Kỹ thuật phần mềm", "Xử lý ngôn ngữ tự nhiên"},
                {"Công nghệ phần mềm", "Kỹ thuật phần mềm", "Phát triển ứng dụng web", "Phát triển ứng dụng di động"},
                {"Kỹ thuật máy tính", "Kỹ thuật viễn thông", "Kỹ thuật mạng", "Kỹ thuật phần mềm nhúng"}
        };

        for (long facultyId = 1; facultyId <= majorsByFaculty.length; facultyId++) {
            Faculty faculty = facultyDAO.getById(facultyId);
            for (String majorName : majorsByFaculty[(int) facultyId - 1]) {
                Major major = new Major();
                major.setName(majorName);
                major.setFacultyId(faculty.getId());
                majorDAO.insert(major);
            }
        }
    }

    private static void insertClassList(Context context) {
        AcademicYearDAO academicYearDAO = getInstance(context).academicYearDAO();
        MajorDAO majorDAO = getInstance(context).majorDAO();
        ClassDAO classDAO = getInstance(context).classDAO();
        List<AcademicYear> academicYears = academicYearDAO.getAll();
        List<Major> majors = majorDAO.getAll();
        Random random = new Random();

        for (Major major : majors) {
            for (AcademicYear academicYear : academicYears) {
                for (int i = 1; i <= 3; i++) {
                    Class clazz = new Class();
                    clazz.setName("Lớp " + i + " - " + major.getName() + " - " + academicYear.getName());
                    clazz.setMajorId(major.getId());
                    clazz.setAcademicYearId(academicYear.getId());
                    clazz.setLecturerId(1 + random.nextInt(10));

                    classDAO.insert(clazz);
                }
            }
        }
    }

    private static void insertSubjectList(Context context) {
        SubjectDAO subjectDAO = getInstance(context).subjectDAO();
        MajorDAO majorDAO = getInstance(context).majorDAO();
        List<Major> majors = majorDAO.getAll();

        String[] subjects = {
                "Nhập môn Công nghệ thông tin",
                "Lập trình Java",
                "Cơ sở dữ liệu",
                "Mạng máy tính",
                "Hệ điều hành",
                "Kỹ thuật phần mềm",
                "Cấu trúc dữ liệu và Giải thuật",
                "An ninh mạng",
                "Trí tuệ nhân tạo",
                "Khoa học dữ liệu"
        };

        int[] credits = {3, 4, 3, 3, 3, 4, 4, 3, 4, 3};

        for (Major major : majors) {
            for (int i = 0; i < subjects.length; i++) {
                Subject subject = new Subject();
                subject.setName(subjects[i]);
                subject.setCredits(credits[i]);
                subject.setMajorId(major.getId());

                subjectDAO.insert(subject);
            }
        }
    }

    private static void insertUser(Context context) {
        AcademicYearDAO academicYearDAO = getInstance(context).academicYearDAO();
        LecturerDAO lecturerDAO = getInstance(context).lecturerDAO();
        StudentDAO studentDAO = getInstance(context).studentDAO();
        MajorDAO majorDAO = getInstance(context).majorDAO();
        UserDAO userDAO = getInstance(context).userDAO();

        User admin = new User();
        admin.setFullName("Administrator");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(Utils.hashPassword("admin@123"));
        admin.setAvatar(Utils.getBytesFromDrawable(context, R.drawable.default_avatar));
        admin.setRole(Constants.Role.ADMIN);
        userDAO.insert(admin);

        String[] lecturerEmails = {
                "lecturer1@gmail.com", "lecturer2@gmail.com",
                "lecturer3@gmail.com", "lecturer4@gmail.com",
                "lecturer5@gmail.com", "lecturer6@gmail.com",
                "lecturer7@gmail.com", "lecturer8@gmail.com",
                "lecturer9@gmail.com", "lecturer0@gmail.com"
        };
        String[] lecturerNames = {
                "John Doe", "Jane Smith", "Robert Brown", "Emily Jones", "Michael White",
                "Susan Davis", "William Miller", "Olivia Garcia", "James Martin", "Isabella Wilson"
        };
        for (int i = 0; i < lecturerEmails.length; i++) {
            User user = getUser(context, lecturerNames[i], lecturerEmails[i], Constants.Role.LECTURER, i);
            long userId = userDAO.insert(user);

            Lecturer lecturer = new Lecturer();
            lecturer.setSpecialization("Chuyên ngành " + (i + 1));
            lecturer.setDegree("Bằng cấp " + (i + 1));
            lecturer.setCertificate("Chứng chỉ " + (i + 1));
            lecturer.setUserId(userId);

            lecturerDAO.insert(lecturer);
        }

        String[] studentEmails = {
                "student1@gmail.com", "student2@gmail.com",
                "student3@gmail.com", "student4@gmail.com",
                "student5@gmail.com", "student6@gmail.com",
                "student7@gmail.com", "student8@gmail.com",
                "student9@gmail.com", "student0@gmail.com"
        };
        String[] studentNames = {
                "Nguyễn Văn A", "Trần Thị B", "Lê Văn C", "Phạm Thị D", "Hoàng Văn E",
                "Nguyễn Thị F", "Lê Thị G", "Trương Văn H", "Bùi Thị I", "Đỗ Văn J"
        };

        List<AcademicYear> academicYears = academicYearDAO.getAll();
        List<Major> majors = majorDAO.getAll();

        for (int i = 0; i < studentNames.length; i++) {
            User user = getUser(context, studentNames[i], studentEmails[i], Constants.Role.STUDENT, i);
            Calendar calendar = Calendar.getInstance();
            calendar.set(2000 + i, i % 12, 1);
            user.setDob(calendar.getTime());
            long userId = userDAO.insert(user);

            Student student = new Student();
            student.setSpecialization("Chuyên ngành " + (i + 1));
            student.setMajorId(majors.get(i % majors.size()).getId());
            student.setAcademicYearId(academicYears.get(i % academicYears.size()).getId());
            student.setUserId(userId);

            studentDAO.insert(student);
        }
    }

    private static @NonNull User getUser(Context context, String name, String email, Constants.Role role, int i) {
        User user = new User();
        user.setFullName(name);
        user.setDob(new Date());
        user.setGender(i % 2 == 0);
        user.setAddress("Địa chỉ " + (i + 1));
        user.setEmail(email);
        user.setPassword(Utils.hashPassword("user@123"));
        user.setAvatar(Utils.getBytesFromDrawable(context, R.drawable.default_avatar));
        user.setRole(role);
        user.setFacultyId(i % 4 + 1L);

        return user;
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
