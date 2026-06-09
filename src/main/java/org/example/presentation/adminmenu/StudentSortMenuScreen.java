package org.example.presentation.adminmenu;

import org.example.entity.Student;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.service.StudentService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.ArrayList;
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
                case 1 -> showStudents("SAP XEP HOC VIEN - TEN TANG DAN", studentService.sortStudentsByName(true));
                case 2 -> showStudents("SAP XEP HOC VIEN - TEN GIAM DAN", studentService.sortStudentsByName(false));
                case 3 -> showStudents("SAP XEP HOC VIEN - ID TANG DAN", studentService.sortStudentsById(true));
                case 4 -> showStudents("SAP XEP HOC VIEN - ID GIAM DAN", studentService.sortStudentsById(false));
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void showStudents(String title, List<Student> students) {
        List<List<String>> rows = new ArrayList<>();
        for (Student student : students) {
            rows.add(
                    List.of(
                            String.valueOf(student.getId()),
                            student.getName(),
                            student.getDob().toString(),
                            student.isSex() ? "Nam" : "Nu",
                            student.getEmail(),
                            student.getPhone() == null ? "" : student.getPhone(),
                            student.getCreatedAt().toString()));
        }
        if (rows.isEmpty()) {
            rows.add(List.of("Chua co du lieu", "", "", "", "", "", ""));
        }
        showTable(
                title,
                List.of("ID", "Ho ten", "Ngay sinh", "Gioi tinh", "Email", "So dien thoai", "Ngay tao"),
                rows);
    }
}
