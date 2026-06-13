package org.example.presentation.adminmenu;

import org.example.entity.Student;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.service.student.StudentService;
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
        Integer studentId = promptPositiveIntOrCancel("Nhap id hoc vien can chinh sua: ");
        if (studentId == null) {
            printer.printMessage("Da huy thao tac chinh sua hoc vien.");
            return ScreenResult.STUDENT_MANAGEMENT_MENU;
        }
        if (studentService.findStudentById(studentId).isEmpty()) {
            printer.printMessage("Khong tim thay hoc vien voi id " + studentId + ".");
            return ScreenResult.STUDENT_MANAGEMENT_MENU;
        }
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
                case 1 -> handleNameEdit(studentId);
                case 2 -> handleDobEdit(studentId);
                case 3 -> handleSexEdit(studentId);
                case 4 -> handleEmailEdit(studentId);
                case 5 -> handlePhoneEdit(studentId);
                case 6 -> handlePasswordEdit(studentId);
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void handleNameEdit(int id) {
        try {
            String name = promptRequiredLineOrCancel("Nhap ho ten moi: ");
            if (name == null) {
                showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
                return;
            }
            Student updated = studentService.updateStudentName(id, name);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat ho ten thanh: " + updated.getName());
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }

    private void handleDobEdit(int id) {
        try {
            LocalDate dob = promptDateOrCancel("Nhap ngay sinh moi (yyyy-MM-dd): ");
            if (dob == null) {
                showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
                return;
            }
            Student updated = studentService.updateStudentDob(id, dob);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat ngay sinh cho hoc vien #" + updated.getId() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }

    private void handleSexEdit(int id) {
        try {
            Boolean sex = promptYesNoOrCancel("Gioi tinh moi", "Nam", "Nu", false);
            if (sex == null) {
                showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
                return;
            }
            Student updated = studentService.updateStudentSex(id, sex);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat gioi tinh cho hoc vien #" + updated.getId() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }

    private void handleEmailEdit(int id) {
        try {
            String email = promptEmailOrCancel("Nhap email moi (@gmail.com): ");
            if (email == null) {
                showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
                return;
            }
            Student updated = studentService.updateStudentEmail(id, email);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat email thanh: " + updated.getEmail());
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }

    private void handlePhoneEdit(int id) {
        try {
            String phone = promptPhoneOrCancel("Nhap so dien thoai moi (co the de trong, 10 chu so, bat dau bang 0): ");
            if (phone == null) {
                showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
                return;
            }
            Student updated = studentService.updateStudentPhone(id, phone);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat so dien thoai cho hoc vien #" + updated.getId() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }

    private void handlePasswordEdit(int id) {
        try {
            String password = promptRequiredLineOrCancel("Nhap mat khau moi: ");
            if (password == null) {
                showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
                return;
            }
            Student updated = studentService.updateStudentPassword(id, password);
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da cap nhat mat khau cho hoc vien #" + updated.getId() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", exception.getMessage());
        }
    }
}
