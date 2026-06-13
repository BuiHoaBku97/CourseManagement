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
        Integer id = promptPositiveIntOrCancel("Nhap id hoc vien: ");
        if (id == null) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
            return;
        }
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

    private void handleDobEdit() {
        Integer id = promptPositiveIntOrCancel("Nhap id hoc vien: ");
        if (id == null) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
            return;
        }
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

    private void handleSexEdit() {
        Integer id = promptPositiveIntOrCancel("Nhap id hoc vien: ");
        if (id == null) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
            return;
        }
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

    private void handleEmailEdit() {
        Integer id = promptPositiveIntOrCancel("Nhap id hoc vien: ");
        if (id == null) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
            return;
        }
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

    private void handlePhoneEdit() {
        Integer id = promptPositiveIntOrCancel("Nhap id hoc vien: ");
        if (id == null) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
            return;
        }
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

    private void handlePasswordEdit() {
        Integer id = promptPositiveIntOrCancel("Nhap id hoc vien: ");
        if (id == null) {
            showMessagePlaceholder("CHINH SUA HOC VIEN", "Da huy thao tac chinh sua hoc vien.");
            return;
        }
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
