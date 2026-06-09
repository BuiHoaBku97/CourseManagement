package org.example.presentation.adminmenu;

import org.example.entity.Student;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.service.StudentService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;
import org.example.utils.InputValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StudentManagementMenuScreen extends AbstractMenuScreen {
    private final StudentService studentService;

    public StudentManagementMenuScreen(ConsoleInput input, ConsolePrinter printer, StudentService studentService) {
        super(input, printer);
        this.studentService = Objects.requireNonNull(studentService, "studentService");
    }

    @Override
    public ScreenResult show() {
        while (true) {
            int choice = promptChoice(
                    "QUAN LY HOC VIEN",
                    List.of(
                            "1. Hien thi danh sach hoc vien",
                            "2. Them moi hoc vien",
                            "3. Chinh sua thong tin hoc vien",
                            "4. Xoa hoc vien theo id",
                            "5. Tim kiem hoc vien theo ten, email hoac ma id",
                            "6. Sap xep hoc vien",
                            "0. Quay lai menu Admin"),
                    0,
                    6);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.ADMIN_MENU;
                }
                case 1 -> showStudents("DANH SACH HOC VIEN", studentService.getAllStudents(), "Chua co hoc vien nao.");
                case 2 -> handleAddStudent();
                case 3 -> {
                    return ScreenResult.STUDENT_EDIT_MENU;
                }
                case 4 -> handleDeleteStudent();
                case 5 -> handleSearchStudent();
                case 6 -> {
                    return ScreenResult.STUDENT_SORT_MENU;
                }
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void handleAddStudent() {
        try {
            String name = input.readRequiredLine("Nhap ho ten: ");
            LocalDate dob = promptDate("Nhap ngay sinh (yyyy-MM-dd): ");
            boolean sex = promptYesNo("Chon gioi tinh", "Nam", "Nu");
            String email = input.readRequiredLine("Nhap email: ");
            String phone = input.readLine("Nhap so dien thoai (co the de trong): ");
            String password = input.readRequiredLine("Nhap mat khau: ");
            Student student = studentService.addStudent(name, dob, sex, email, phone, password);
            showMessagePlaceholder(
                    "THEM MOI HOC VIEN",
                    "Da tao hoc vien #" + student.getId() + " - " + student.getName() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("THEM MOI HOC VIEN", exception.getMessage());
        }
    }

    private void handleDeleteStudent() {
        int id = promptPositiveInt("Nhap id hoc vien can xoa: ");
        try {
            Student student = studentService.findStudentById(id)
                    .orElseThrow(() -> new IllegalStateException("Khong tim thay hoc vien voi id " + id + "."));
            if (!confirmAction("XAC NHAN XOA HOC VIEN " + student.getName())) {
                showMessagePlaceholder("XOA HOC VIEN THEO ID", "Da huy thao tac xoa hoc vien.");
                return;
            }
            studentService.deleteStudent(id);
            showMessagePlaceholder("XOA HOC VIEN THEO ID", "Da xoa hoc vien #" + id + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("XOA HOC VIEN THEO ID", exception.getMessage());
        }
    }

    private void handleSearchStudent() {
        try {
            String query = input.readRequiredLine("Nhap tu khoa tim kiem: ");
            List<Student> students = studentService.searchStudents(query);
            showStudents("TIM KIEM HOC VIEN", students, "Khong tim thay hoc vien phu hop.");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("TIM KIEM HOC VIEN", exception.getMessage());
        }
    }

    private void showStudents(String title, List<Student> students, String emptyMessage) {
        List<List<String>> rows = new ArrayList<>();
        for (Student student : students) {
            rows.add(
                    List.of(
                            String.valueOf(student.getId()),
                            student.getName(),
                            student.getDob().toString(),
                            student.isSex() ? "Nam" : "Nu",
                            student.getEmail(),
                            String.valueOf(student.getPhone() == null ? "" : student.getPhone()),
                            student.getCreatedAt().toString()));
        }
        if (rows.isEmpty()) {
            rows.add(List.of(emptyMessage, "", "", "", "", "", ""));
        }
        showTable(
                title,
                List.of("ID", "Ho ten", "Ngay sinh", "Gioi tinh", "Email", "So dien thoai", "Ngay tao"),
                rows);
    }
}
