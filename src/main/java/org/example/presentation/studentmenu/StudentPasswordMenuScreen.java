package org.example.presentation.studentmenu;

import org.example.entity.StudentAccount;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.presentation.StudentSessionContext;
import org.example.service.student.StudentPortalService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.Objects;

public final class StudentPasswordMenuScreen extends AbstractMenuScreen {
    private final StudentPortalService studentPortalService;
    private final StudentSessionContext studentSessionContext;

    public StudentPasswordMenuScreen(
            ConsoleInput input,
            ConsolePrinter printer,
            StudentPortalService studentPortalService,
            StudentSessionContext studentSessionContext) {
        super(input, printer);
        this.studentPortalService = Objects.requireNonNull(studentPortalService, "studentPortalService");
        this.studentSessionContext = Objects.requireNonNull(studentSessionContext, "studentSessionContext");
    }

    @Override
    public ScreenResult show() {
        StudentAccount currentStudent = requireCurrentStudent();
        handleChangePassword(currentStudent.getId());
        return ScreenResult.STUDENT_MENU;
    }

    private void handleChangePassword(int studentId) {
        try {
            String currentPassword = input.readRequiredLine("Nhap mat khau hien tai: ");
            studentPortalService.validateCurrentPassword(studentId, currentPassword);
            String newPassword = input.readRequiredLine("Nhap mat khau moi: ");
            studentPortalService.validateNewPassword(newPassword);
            studentPortalService.requestPasswordChangeOtp(studentId);
            printer.printMessage("Ma OTP da duoc gui den email dang ky.");
            String inputOtp = input.readRequiredLine("Nhap ma OTP: ");
            studentPortalService.updatePassword(studentId, inputOtp, currentPassword, newPassword);
            showMessagePlaceholder("CAP NHAT MAT KHAU", "Da cap nhat mat khau thanh cong.");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("CAP NHAT MAT KHAU", exception.getMessage());
        }
    }

    private StudentAccount requireCurrentStudent() {
        return studentSessionContext.getCurrentStudent()
                .orElseThrow(() -> new IllegalStateException("Khong co thong tin hoc vien dang nhap."));
    }
}
