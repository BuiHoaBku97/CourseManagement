package org.example.presentation.adminmenu;

import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.service.student.StudentService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;
import java.util.Objects;

public final class StudentSortMenuScreen extends AbstractMenuScreen {
    private final StudentService studentService;

    public StudentSortMenuScreen(ConsoleInput input, ConsolePrinter printer, StudentService studentService) {
        super(input, printer);
        this.studentService = Objects.requireNonNull(studentService, "studentService");
    }

    @Override
    public ScreenResult show() {
        while (true) {
            int choice = promptChoice(
                    "SAP XEP HOC VIEN",
                    List.of(
                            "1. Sap xep theo ten tang dan",
                            "2. Sap xep theo ten giam dan",
                            "3. Sap xep theo id tang dan",
                            "4. Sap xep theo id giam dan",
                            "0. Quay lai"),
                    0,
                    4);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.STUDENT_MANAGEMENT_MENU;
                }
                case 1 -> showPagedStudents(
                        "SAP XEP HOC VIEN - TEN TANG DAN",
                        request -> studentService.sortStudentsByName(true, request));
                case 2 -> showPagedStudents(
                        "SAP XEP HOC VIEN - TEN GIAM DAN",
                        request -> studentService.sortStudentsByName(false, request));
                case 3 -> showPagedStudents(
                        "SAP XEP HOC VIEN - ID TANG DAN",
                        request -> studentService.sortStudentsById(true, request));
                case 4 -> showPagedStudents(
                        "SAP XEP HOC VIEN - ID GIAM DAN",
                        request -> studentService.sortStudentsById(false, request));
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void showPagedStudents(
            String title,
            java.util.function.Function<org.example.common.PageRequest, org.example.common.Page<org.example.entity.Student>> pageLoader) {
        showPagedTable(title, StudentTableRows.HEADERS, "Chua co du lieu", pageLoader, StudentTableRows::toRow);
    }
}
