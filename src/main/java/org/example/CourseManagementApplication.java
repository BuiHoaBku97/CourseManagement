package org.example;

import org.example.dao.AdminAccountDao;
import org.example.dao.CourseDao;
import org.example.dao.EnrollmentDao;
import org.example.dao.StudentAccountDao;
import org.example.dao.StudentDao;
import org.example.dao.StatisticsDao;
import org.example.presentation.AdminLoginScreen;
import org.example.presentation.adminmenu.AdminMenuScreen;
import org.example.presentation.adminmenu.CourseEditMenuScreen;
import org.example.presentation.adminmenu.CourseMenuScreen;
import org.example.presentation.adminmenu.CourseSortMenuScreen;
import org.example.presentation.adminmenu.EnrollmentMenuScreen;
import org.example.presentation.Screen;
import org.example.presentation.ScreenResult;
import org.example.presentation.StartupScreen;
import org.example.presentation.StudentLoginScreen;
import org.example.presentation.StudentSessionContext;
import org.example.presentation.adminmenu.StudentEditMenuScreen;
import org.example.presentation.adminmenu.StudentManagementMenuScreen;
import org.example.presentation.studentmenu.StudentMenuScreen;
import org.example.presentation.studentmenu.StudentCourseMenuScreen;
import org.example.presentation.studentmenu.StudentRegisterCourseMenuScreen;
import org.example.presentation.studentmenu.StudentPasswordMenuScreen;
import org.example.presentation.studentmenu.StudentRegisteredCourseMenuScreen;
import org.example.presentation.studentmenu.StudentCancelRegistrationMenuScreen;
import org.example.presentation.adminmenu.StudentSortMenuScreen;
import org.example.presentation.adminmenu.StatisticsMenuScreen;
import org.example.service.AuthenticationService;
import org.example.service.CourseService;
import org.example.service.EnrollmentService;
import org.example.service.JdbcCourseService;
import org.example.service.JdbcEnrollmentService;
import org.example.service.JdbcAuthenticationService;
import org.example.service.GmailSmtpOtpSender;
import org.example.service.JdbcStatisticsService;
import org.example.service.JdbcStudentPortalService;
import org.example.service.JdbcStudentService;
import org.example.service.StatisticsService;
import org.example.service.StudentService;
import org.example.service.StudentPortalService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;
import org.example.utils.JdbcConnectionFactory;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class CourseManagementApplication {
    private final Map<ScreenResult, Screen> screens;
    private final ConsolePrinter printer;
    private final StudentSessionContext studentSessionContext;

    public CourseManagementApplication() {
        this(System.in, System.out);
    }

    public CourseManagementApplication(InputStream inputStream, PrintStream printStream) {
        ConsoleInput input = new ConsoleInput(inputStream, printStream);
        this.printer = new ConsolePrinter(printStream);
        this.studentSessionContext = new StudentSessionContext();
        JdbcConnectionFactory connectionFactory = new JdbcConnectionFactory();
        AuthenticationService authenticationService = new JdbcAuthenticationService(new AdminAccountDao(connectionFactory),
                                                                                    new StudentAccountDao(connectionFactory));
        CourseService courseService = new JdbcCourseService(new CourseDao(connectionFactory));
        StudentService studentService = new JdbcStudentService(new StudentDao(connectionFactory));
        EnrollmentService enrollmentService = new JdbcEnrollmentService(new EnrollmentDao(connectionFactory));
        StatisticsService statisticsService = new JdbcStatisticsService(new StatisticsDao(connectionFactory));
        StudentPortalService studentPortalService =
                new JdbcStudentPortalService(
                        new CourseDao(connectionFactory),
                        new EnrollmentDao(connectionFactory),
                        new StudentDao(connectionFactory),
                        GmailSmtpOtpSender.fromEnvironment());
        this.screens = new EnumMap<>(ScreenResult.class);
        this.screens.put(ScreenResult.STARTUP, new StartupScreen(input, printer));
        this.screens.put(ScreenResult.ADMIN_LOGIN, new AdminLoginScreen(input, printer, authenticationService));
        this.screens.put(
                ScreenResult.STUDENT_LOGIN,
                new StudentLoginScreen(input, printer, authenticationService, studentSessionContext));
        this.screens.put(ScreenResult.ADMIN_MENU, new AdminMenuScreen(input, printer));
        this.screens.put(ScreenResult.COURSE_MENU, new CourseMenuScreen(input, printer, courseService));
        this.screens.put(ScreenResult.COURSE_EDIT_MENU, new CourseEditMenuScreen(input, printer, courseService));
        this.screens.put(ScreenResult.COURSE_SORT_MENU, new CourseSortMenuScreen(input, printer, courseService));
        this.screens.put(ScreenResult.STUDENT_MANAGEMENT_MENU, new StudentManagementMenuScreen(input, printer, studentService));
        this.screens.put(ScreenResult.STUDENT_EDIT_MENU, new StudentEditMenuScreen(input, printer, studentService));
        this.screens.put(ScreenResult.STUDENT_SORT_MENU, new StudentSortMenuScreen(input, printer, studentService));
        this.screens.put(ScreenResult.ENROLLMENT_MENU, new EnrollmentMenuScreen(input, printer, enrollmentService));
        this.screens.put(ScreenResult.STATISTICS_MENU, new StatisticsMenuScreen(input, printer, statisticsService));
        this.screens.put(
                ScreenResult.STUDENT_MENU,
                new StudentMenuScreen(input, printer, studentSessionContext));
        this.screens.put(
                ScreenResult.STUDENT_COURSE_MENU,
                new StudentCourseMenuScreen(input, printer, studentPortalService, studentSessionContext));
        this.screens.put(
                ScreenResult.STUDENT_REGISTER_COURSE_MENU,
                new StudentRegisterCourseMenuScreen(input, printer, studentPortalService, studentSessionContext));
        this.screens.put(
                ScreenResult.STUDENT_REGISTERED_COURSE_MENU,
                new StudentRegisteredCourseMenuScreen(input, printer, studentPortalService, studentSessionContext));
        this.screens.put(
                ScreenResult.STUDENT_CANCEL_REGISTRATION_MENU,
                new StudentCancelRegistrationMenuScreen(input, printer, studentPortalService, studentSessionContext));
        this.screens.put(
                ScreenResult.STUDENT_PASSWORD_MENU,
                new StudentPasswordMenuScreen(input, printer, studentPortalService, studentSessionContext));
    }

    public static void main(String[] args) {
        new CourseManagementApplication().run();
    }

    public void run() {
        ScreenResult current = ScreenResult.STARTUP;
        while (current != ScreenResult.EXIT) {
            Screen screen = screens.get(current);
            if (screen == null) {
                throw new IllegalStateException("Khong tim thay man hinh: " + current);
            }
            current = Objects.requireNonNull(screen.show(), "Screen result must not be null.");
        }
        printer.printMessage("Da thoat chuong trinh.");
    }
}
