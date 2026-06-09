package org.example.presentation.adminmenu;

import org.example.entity.Student;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.service.StudentService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public final class StudentEditMenuScreen extends AbstractMenuScreen {
    private final StudentService studentService;

    public StudentEditMenuScreen(ConsoleInput input, ConsolePrinter printer, StudentService studentService) {
        super(input, printer);
        this.studentService = Objects.requireNonNull(studentService, "studentService");
    }

    @Override
    public ScreenResult show() {
        while (true) {
            int choice = promptChoice(
                    "CHINH SUA HOC VIEN",
                    List.of(
                            "1. Sua ho ten",
                            "2. Sua ngay sinh",
                            "3. Sua gioi tinh",
                            "4. Sua email",
                            "5. Sua so dien thoai",
                            "6. Sua mat khau",
                            "0. Quay lai"),
                    0,
                    6);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.STUDENT_MANAGEMENT_MENU;
                }
                case 1 -> handleNameEdit();
                case 2 -> handleDobEdit();
                case 3 -> handleSexEdit();
                case 4 -> handleEmailEdit();
                case 5 -> handlePhoneEdit();
                case 6 -> handlePasswordEdit();
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void handleNameEdit() {
        int id = promptPositiveInt("Nhap id hoc vien: ");
        try {
            String name = input.readRequiredLine("Nhap ho ten moi: ");
            Student updated = studentService.updateStudentName(id, name);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat ho ten thanh: " + updated.getName());
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }

    private void handleDobEdit() {
        int id = promptPositiveInt("Nhap id hoc vien: ");
        try {
            LocalDate dob = promptDate("Nhap ngay sinh moi (yyyy-MM-dd): ");
            Student updated = studentService.updateStudentDob(id, dob);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat ngay sinh cho hoc vien #" + updated.getId() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }

    private void handleSexEdit() {
        int id = promptPositiveInt("Nhap id hoc vien: ");
        try {
            boolean sex = promptYesNo("Chon gioi tinh moi", "Nam", "Nu");
            Student updated = studentService.updateStudentSex(id, sex);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat gioi tinh cho hoc vien #" + updated.getId() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }

    private void handleEmailEdit() {
        int id = promptPositiveInt("Nhap id hoc vien: ");
        try {
            String email = input.readRequiredLine("Nhap email moi: ");
            Student updated = studentService.updateStudentEmail(id, email);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat email thanh: " + updated.getEmail());
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }

    private void handlePhoneEdit() {
        int id = promptPositiveInt("Nhap id hoc vien: ");
        try {
            String phone = input.readLine("Nhap so dien thoai moi: ");
            Student updated = studentService.updateStudentPhone(id, phone);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat so dien thoai cho hoc vien #" + updated.getId() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }

    private void handlePasswordEdit() {
        int id = promptPositiveInt("Nhap id hoc vien: ");
        try {
            String password = input.readRequiredLine("Nhap mat khau moi: ");
            Student updated = studentService.updateStudentPassword(id, password);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat mat khau cho hoc vien #" + updated.getId() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }
}
