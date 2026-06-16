package org.example;

import org.example.dao.impl.AdminAccountDao;
import org.example.dao.impl.CourseDao;
import org.example.dao.impl.EnrollmentDao;
import org.example.dao.impl.StatisticsDao;
import org.example.dao.impl.StudentAccountDao;
import org.example.dao.impl.StudentDao;
import org.example.presentation.Screen;
import org.example.presentation.ScreenResult;
import org.example.presentation.StartupScreen;
import org.example.presentation.StudentSessionContext;
import org.example.presentation.adminmenu.AdminLoginScreen;
import org.example.presentation.adminmenu.AdminMenuScreen;
import org.example.presentation.adminmenu.CourseEditMenuScreen;
import org.example.presentation.adminmenu.CourseMenuScreen;
import org.example.presentation.adminmenu.CourseSortMenuScreen;
import org.example.presentation.adminmenu.EnrollmentMenuScreen;
import org.example.presentation.adminmenu.StatisticsMenuScreen;
import org.example.presentation.adminmenu.StudentEditMenuScreen;
import org.example.presentation.adminmenu.StudentManagementMenuScreen;
import org.example.presentation.adminmenu.StudentSortMenuScreen;
import org.example.presentation.studentmenu.StudentCancelRegistrationMenuScreen;
import org.example.presentation.studentmenu.StudentCourseMenuScreen;
import org.example.presentation.studentmenu.StudentLoginScreen;
import org.example.presentation.studentmenu.StudentMenuScreen;
import org.example.presentation.studentmenu.StudentPasswordMenuScreen;
import org.example.presentation.studentmenu.StudentRegisterCourseMenuScreen;
import org.example.presentation.studentmenu.StudentRegisteredCourseMenuScreen;
import org.example.service.admin.CourseService;
import org.example.service.admin.EnrollmentService;
import org.example.service.admin.StatisticsService;
import org.example.service.admin.impl.JdbcCourseService;
import org.example.service.admin.impl.JdbcEnrollmentService;
import org.example.service.admin.impl.JdbcStatisticsService;
import org.example.service.auth.AuthenticationService;
import org.example.service.auth.impl.JdbcAuthenticationService;
import org.example.service.student.StudentPortalService;
import org.example.service.student.StudentService;
import org.example.service.student.impl.GmailSmtpOtpSender;
import org.example.service.student.impl.JdbcStudentPortalService;
import org.example.service.student.impl.JdbcStudentService;
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

    public CourseManagementApplication() {
        this(System.in, System.out);
    }

    public CourseManagementApplication(InputStream inputStream, PrintStream printStream) {
        ApplicationContext applicationContext = createApplicationContext(inputStream, printStream);
        this.printer = applicationContext.printer();
        this.screens = createScreens(applicationContext);
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

    private static ApplicationContext createApplicationContext(InputStream inputStream, PrintStream printStream) {
        ConsoleInput input = new ConsoleInput(inputStream, printStream);
        ConsolePrinter printer = new ConsolePrinter(printStream);
        StudentSessionContext studentSessionContext = new StudentSessionContext();
        JdbcConnectionFactory connectionFactory = new JdbcConnectionFactory();
        AuthenticationService authenticationService = createAuthenticationService(connectionFactory);
        CourseService courseService = new JdbcCourseService(new CourseDao(connectionFactory));
        StudentService studentService = new JdbcStudentService(new StudentDao(connectionFactory));
        EnrollmentService enrollmentService = new JdbcEnrollmentService(new EnrollmentDao(connectionFactory));
        StatisticsService statisticsService = new JdbcStatisticsService(new StatisticsDao(connectionFactory));
        StudentPortalService studentPortalService = createStudentPortalService(connectionFactory);
        return new ApplicationContext(
                input,
                printer,
                studentSessionContext,
                authenticationService,
                courseService,
                studentService,
                enrollmentService,
                statisticsService,
                studentPortalService);
    }

    private static AuthenticationService createAuthenticationService(JdbcConnectionFactory connectionFactory) {
        return new JdbcAuthenticationService(
                new AdminAccountDao(connectionFactory),
                new StudentAccountDao(connectionFactory));
    }

    private static StudentPortalService createStudentPortalService(JdbcConnectionFactory connectionFactory) {
        return new JdbcStudentPortalService(
                new CourseDao(connectionFactory),
                new EnrollmentDao(connectionFactory),
                new StudentDao(connectionFactory),
                GmailSmtpOtpSender.fromEnvironment());
    }

    private static Map<ScreenResult, Screen> createScreens(ApplicationContext applicationContext) {
        Map<ScreenResult, Screen> screens = new EnumMap<>(ScreenResult.class);
        ConsoleInput input = applicationContext.input();
        ConsolePrinter printer = applicationContext.printer();
        StudentSessionContext studentSessionContext = applicationContext.studentSessionContext();
        AuthenticationService authenticationService = applicationContext.authenticationService();
        CourseService courseService = applicationContext.courseService();
        StudentService studentService = applicationContext.studentService();
        EnrollmentService enrollmentService = applicationContext.enrollmentService();
        StatisticsService statisticsService = applicationContext.statisticsService();
        StudentPortalService studentPortalService = applicationContext.studentPortalService();

        screens.put(ScreenResult.STARTUP, new StartupScreen(input, printer));
        screens.put(ScreenResult.ADMIN_LOGIN, new AdminLoginScreen(input, printer, authenticationService));
        screens.put(
                ScreenResult.STUDENT_LOGIN,
                new StudentLoginScreen(input, printer, authenticationService, studentSessionContext));
        screens.put(ScreenResult.ADMIN_MENU, new AdminMenuScreen(input, printer));
        screens.put(ScreenResult.COURSE_MENU, new CourseMenuScreen(input, printer, courseService));
        screens.put(ScreenResult.COURSE_EDIT_MENU, new CourseEditMenuScreen(input, printer, courseService));
        screens.put(ScreenResult.COURSE_SORT_MENU, new CourseSortMenuScreen(input, printer, courseService));
        screens.put(
                ScreenResult.STUDENT_MANAGEMENT_MENU,
                new StudentManagementMenuScreen(input, printer, studentService));
        screens.put(ScreenResult.STUDENT_EDIT_MENU, new StudentEditMenuScreen(input, printer, studentService));
        screens.put(ScreenResult.STUDENT_SORT_MENU, new StudentSortMenuScreen(input, printer, studentService));
        screens.put(ScreenResult.ENROLLMENT_MENU, new EnrollmentMenuScreen(input, printer, enrollmentService));
        screens.put(ScreenResult.STATISTICS_MENU, new StatisticsMenuScreen(input, printer, statisticsService));
        screens.put(ScreenResult.STUDENT_MENU, new StudentMenuScreen(input, printer, studentSessionContext));
        screens.put(
                ScreenResult.STUDENT_COURSE_MENU,
                new StudentCourseMenuScreen(input, printer, studentPortalService, studentSessionContext));
        screens.put(
                ScreenResult.STUDENT_REGISTER_COURSE_MENU,
                new StudentRegisterCourseMenuScreen(input, printer, studentPortalService, studentSessionContext));
        screens.put(
                ScreenResult.STUDENT_REGISTERED_COURSE_MENU,
                new StudentRegisteredCourseMenuScreen(input, printer, studentPortalService, studentSessionContext));
        screens.put(
                ScreenResult.STUDENT_CANCEL_REGISTRATION_MENU,
                new StudentCancelRegistrationMenuScreen(input, printer, studentPortalService, studentSessionContext));
        screens.put(
                ScreenResult.STUDENT_PASSWORD_MENU,
                new StudentPasswordMenuScreen(input, printer, studentPortalService, studentSessionContext));
        return screens;
    }

    private record ApplicationContext(
            ConsoleInput input,
            ConsolePrinter printer,
            StudentSessionContext studentSessionContext,
            AuthenticationService authenticationService,
            CourseService courseService,
            StudentService studentService,
            EnrollmentService enrollmentService,
            StatisticsService statisticsService,
            StudentPortalService studentPortalService) {
    }
}
