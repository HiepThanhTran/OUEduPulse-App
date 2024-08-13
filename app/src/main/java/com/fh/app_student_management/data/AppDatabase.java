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
import com.fh.app_student_management.data.dao.CrossRefDAO;
import com.fh.app_student_management.data.dao.FacultyDAO;
import com.fh.app_student_management.data.dao.LecturerDAO;
import com.fh.app_student_management.data.dao.MajorDAO;
import com.fh.app_student_management.data.dao.ScoreDAO;
import com.fh.app_student_management.data.dao.SemesterDAO;
import com.fh.app_student_management.data.dao.StatisticalDAO;
import com.fh.app_student_management.data.dao.StudentDAO;
import com.fh.app_student_management.data.dao.SubjectDAO;
import com.fh.app_student_management.data.dao.UserDAO;
import com.fh.app_student_management.data.entities.AcademicYear;
import com.fh.app_student_management.data.entities.Class;
import com.fh.app_student_management.data.entities.Faculty;
import com.fh.app_student_management.data.entities.Lecturer;
import com.fh.app_student_management.data.entities.LecturerSubjectCrossRef;
import com.fh.app_student_management.data.entities.Major;
import com.fh.app_student_management.data.entities.Score;
import com.fh.app_student_management.data.entities.Semester;
import com.fh.app_student_management.data.entities.Student;
import com.fh.app_student_management.data.entities.StudentSemesterCrossRef;
import com.fh.app_student_management.data.entities.StudentSubjectCrossRef;
import com.fh.app_student_management.data.entities.Subject;
import com.fh.app_student_management.data.entities.SubjectSemesterCrossRef;
import com.fh.app_student_management.data.entities.User;
import com.fh.app_student_management.utilities.Constants;
import com.fh.app_student_management.utilities.Utils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Database(entities = {
        AcademicYear.class,
        Class.class,
        Faculty.class,
        Lecturer.class,
        Major.class,
        Score.class,
        Semester.class,
        Student.class,
        Subject.class,
        User.class,
        LecturerSubjectCrossRef.class,
        StudentSemesterCrossRef.class,
        StudentSubjectCrossRef.class,
        SubjectSemesterCrossRef.class
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
        insertFacultyList(context);
        insertSemesterList(context);
        insertMajorList(context);
        insertClassList(context);
        insertUser(context);
        insertStudentsToClass(context);
        insertSubjectList(context);
        insertCrossRefList(context);
    }

    private static void insertAcademicYearList(Context context) {
        AppDatabase db = getInstance(context);

        for (int i = 2021; i <= 2025; i++) {
            AcademicYear academicYear = new AcademicYear();
            academicYear.setName("Khóa " + i);
            Calendar startCal = Calendar.getInstance();
            startCal.set(i, Calendar.SEPTEMBER, 1);
            academicYear.setStartDate(startCal.getTime());

            Calendar endCal = Calendar.getInstance();
            endCal.set(i + 4, Calendar.AUGUST, 31);
            academicYear.setEndDate(endCal.getTime());

            db.academicYearDAO().insert(academicYear);
        }
    }

    private static void insertFacultyList(Context context) {
        AppDatabase db = getInstance(context);

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

            db.facultyDAO().insert(faculty);
        }
    }

    private static void insertSemesterList(Context context) {
        AppDatabase db = getInstance(context);
        List<AcademicYear> academicYears = db.academicYearDAO().getAll();
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
                db.semesterDAO().insert(semester);
            }
        }
    }

    private static void insertMajorList(Context context) {
        AppDatabase db = getInstance(context);

        String[][] majorsByFaculty = {
                {"Công nghệ thông tin", "An toàn thông tin", "Hệ thống thông tin", "Mạng máy tính", "Lập trình di động"},
                {"Khoa học máy tính", "Trí tuệ nhân tạo", "Khoa học dữ liệu", "Kỹ thuật phần mềm", "Xử lý ngôn ngữ tự nhiên"},
                {"Công nghệ phần mềm", "Kỹ thuật phần mềm", "Phát triển ứng dụng web", "Phát triển ứng dụng di động"},
                {"Kỹ thuật máy tính", "Kỹ thuật viễn thông", "Kỹ thuật mạng", "Kỹ thuật phần mềm nhúng"}
        };

        for (long facultyId = 1; facultyId <= majorsByFaculty.length; facultyId++) {
            Faculty faculty = db.facultyDAO().getById(facultyId);
            for (String majorName : majorsByFaculty[(int) facultyId - 1]) {
                Major major = new Major();
                major.setName(majorName);
                major.setFacultyId(faculty.getId());

                db.majorDAO().insert(major);
            }
        }
    }

    private static void insertClassList(Context context) {
        AppDatabase db = getInstance(context);
        List<Major> majors = db.majorDAO().getAll();
        List<AcademicYear> academicYears = db.academicYearDAO().getAll();

        for (AcademicYear academicYear : academicYears) {
            for (Major major : majors) {
                Class aClass = new Class();
                aClass.setName(major.getName() + " - " + academicYear.getName());
                aClass.setMajorId(major.getId());
                aClass.setAcademicYearId(academicYear.getId());

                db.classDAO().insert(aClass);
            }
        }
    }

    private static void insertUser(Context context) {
        AppDatabase db = getInstance(context);

        User admin = new User();
        admin.setFullName("Administrator");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(Utils.hashPassword("admin@123"));
        admin.setAvatar(Utils.getBytesFromDrawable(context, R.drawable.default_avatar));
        admin.setRole(Constants.Role.ADMIN);
        db.userDAO().insert(admin);

        String[] specialistEmails = {
                "specialist1@gmail.com", "specialist2@gmail.com",
                "specialist3@gmail.com", "specialist4@gmail.com",
                "specialist5@gmail.com", "specialist6@gmail.com",
                "specialist7@gmail.com", "specialist8@gmail.com",
                "specialist9@gmail.com", "specialist0@gmail.com"
        };
        String[] specialistNames = {
                "John Doe", "Jane Smith", "Robert Brown", "Emily Jones", "Michael White",
                "Susan Davis", "William Miller", "Olivia Garcia", "James Martin", "Isabella Wilson"
        };

        for (int i = 0; i < specialistEmails.length; i++) {
            User user = getUser(context, specialistNames[i], specialistEmails[i], Constants.Role.SPECIALIST, i);
            db.userDAO().insert(user);
        }

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
            long userId = db.userDAO().insert(user);

            Lecturer lecturer = new Lecturer();
            lecturer.setSpecialization("Chuyên ngành " + (i + 1));
            lecturer.setDegree("Bằng cấp " + (i + 1));
            lecturer.setCertificate("Chứng chỉ " + (i + 1));
            lecturer.setUserId(userId);

            db.lecturerDAO().insert(lecturer);
        }

        List<AcademicYear> academicYears = db.academicYearDAO().getAll();
        List<Major> majors = db.majorDAO().getAll();

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

        for (int i = 0; i < studentNames.length; i++) {
            User user = getUser(context, studentNames[i], studentEmails[i], Constants.Role.STUDENT, i);
            Calendar calendar = Calendar.getInstance();
            calendar.set(2000 + i, i % 12, 1);
            user.setDob(calendar.getTime());
            long userId = db.userDAO().insert(user);

            Student student = new Student();
            student.setMajorId(majors.get(i % majors.size()).getId());
            student.setAcademicYearId(academicYears.get(i % academicYears.size()).getId());
            student.setUserId(userId);

            db.studentDAO().insert(student);
        }
    }

    private static void insertStudentsToClass(Context context) {
        AppDatabase db = getInstance(context);
        List<Student> students = db.studentDAO().getAll();

        for (int i = 0; i < students.size(); i++) {
            long classId = (i / 2) + 1;
            Student student = students.get(i);
            student.setClassId(classId);
            db.studentDAO().update(student);
        }
    }

    private static void insertSubjectList(Context context) {
        AppDatabase db = getInstance(context);
        List<Class> classes = db.classDAO().getAll();
        Random random = new Random();

        String[] subjects = {
                "Nhập môn Công nghệ thông tin", "Lập trình Java",
                "Cơ sở dữ liệu", "Mạng máy tính",
                "Hệ điều hành", "Kỹ thuật phần mềm",
                "Cấu trúc dữ liệu và Giải thuật", "An ninh mạng",
                "Trí tuệ nhân tạo", "Khoa học dữ liệu"
        };
        int[] credits = {3, 4, 3, 3, 3, 4, 4, 3, 4, 3};

        for (Class aClass : classes) {
            Set<Integer> selectedIndexes = new HashSet<>();
            while (selectedIndexes.size() < 3) {
                selectedIndexes.add(random.nextInt(subjects.length));
            }

            for (Integer index : selectedIndexes) {
                Subject subject = new Subject();
                subject.setName(subjects[index]);
                subject.setCredits(credits[index]);
                subject.setMajorId(aClass.getMajorId());
                subject.setClassId(aClass.getId());

                db.subjectDAO().insert(subject);
            }
        }
    }

    private static void insertCrossRefList(Context context) {
        AppDatabase db = getInstance(context);
        List<Semester> semesters = db.semesterDAO().getAll();
        List<Student> students = db.studentDAO().getAll();
        List<Subject> subjects = db.subjectDAO().getAll();
        Random random = new Random();

        for (Semester semester : semesters) {
            Collections.shuffle(subjects, random);

            for (Student student : students) {
                StudentSemesterCrossRef crossRef = new StudentSemesterCrossRef();
                crossRef.setStudentId(student.getId());
                crossRef.setSemesterId(semester.getId());
                db.crossRefDAO().insertStudentSemesterCrossRef(crossRef);
            }

            for (int i = 0; i < 3; i++) {
                SubjectSemesterCrossRef subjectSemesterCrossRef = new SubjectSemesterCrossRef();
                subjectSemesterCrossRef.setSubjectId(subjects.get(i).getId());
                subjectSemesterCrossRef.setSemesterId(semester.getId());
                db.crossRefDAO().insertSubjectSemesterCrossRef(subjectSemesterCrossRef);

                LecturerSubjectCrossRef lecturerSubjectCrossRef = new LecturerSubjectCrossRef();
                lecturerSubjectCrossRef.setLecturerId(1);
                lecturerSubjectCrossRef.setSubjectId(subjects.get(i).getId());
                db.crossRefDAO().insertLecturerSubjectCrossRef(lecturerSubjectCrossRef);
            }
        }

        List<LecturerSubjectCrossRef> allLecturerSubjectCrossRef = db.crossRefDAO().getAllLecturerSubjectCrossRef();
        for (LecturerSubjectCrossRef lecturerSubjectCrossRef : allLecturerSubjectCrossRef) {
            Collections.shuffle(students, random);

            for (int i = 0; i < 4; i++) {
                StudentSubjectCrossRef studentSubjectCrossRef = new StudentSubjectCrossRef();
                studentSubjectCrossRef.setStudentId(students.get(i).getId());
                studentSubjectCrossRef.setSubjectId(lecturerSubjectCrossRef.getSubjectId());
                db.crossRefDAO().insertStudentSubjectCrossRef(studentSubjectCrossRef);
            }
        }
    }

    @NonNull
    private static User getUser(Context context, String name, String email, Constants.Role role, int i) {
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

    public abstract ScoreDAO scoreDAO();

    public abstract SemesterDAO semesterDAO();

    public abstract StudentDAO studentDAO();

    public abstract SubjectDAO subjectDAO();

    public abstract UserDAO userDAO();

    public abstract CrossRefDAO crossRefDAO();

    public abstract StatisticalDAO statisticalDAO();
}