package org.example;

import org.example.presentation.AdminLoginScreen;
import org.example.presentation.AdminMenuScreen;
import org.example.presentation.CourseMenuScreen;
import org.example.presentation.EnrollmentMenuScreen;
import org.example.presentation.Screen;
import org.example.presentation.ScreenResult;
import org.example.presentation.StartupScreen;
import org.example.presentation.StudentLoginScreen;
import org.example.presentation.StudentManagementMenuScreen;
import org.example.presentation.StudentMenuScreen;
import org.example.presentation.StatisticsMenuScreen;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

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
        ConsoleInput input = new ConsoleInput(inputStream, printStream);
        this.printer = new ConsolePrinter(printStream);
        this.screens = new EnumMap<>(ScreenResult.class);
        this.screens.put(ScreenResult.STARTUP, new StartupScreen(input, printer));
        this.screens.put(ScreenResult.ADMIN_LOGIN, new AdminLoginScreen(input, printer));
        this.screens.put(ScreenResult.STUDENT_LOGIN, new StudentLoginScreen(input, printer));
        this.screens.put(ScreenResult.ADMIN_MENU, new AdminMenuScreen(input, printer));
        this.screens.put(ScreenResult.COURSE_MENU, new CourseMenuScreen(input, printer));
        this.screens.put(ScreenResult.STUDENT_MANAGEMENT_MENU, new StudentManagementMenuScreen(input, printer));
        this.screens.put(ScreenResult.ENROLLMENT_MENU, new EnrollmentMenuScreen(input, printer));
        this.screens.put(ScreenResult.STATISTICS_MENU, new StatisticsMenuScreen(input, printer));
        this.screens.put(ScreenResult.STUDENT_MENU, new StudentMenuScreen(input, printer));
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
