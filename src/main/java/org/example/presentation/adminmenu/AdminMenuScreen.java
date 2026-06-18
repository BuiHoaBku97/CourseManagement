package org.example.presentation.adminmenu;

import org.example.entity.AdminAccount;
import org.example.presentation.AbstractMenuScreen;
import org.example.presentation.ScreenResult;
import org.example.service.admin.AdminAccountService;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;
import java.util.Objects;

public final class AdminMenuScreen extends AbstractMenuScreen {
    private final AdminAccountService adminAccountService;

    public AdminMenuScreen(ConsoleInput input, ConsolePrinter printer, AdminAccountService adminAccountService) {
        super(input, printer);
        this.adminAccountService = Objects.requireNonNull(adminAccountService, "adminAccountService");
    }

    @Override
    public ScreenResult show() {
        while (true) {
            int choice = promptChoice(
                    "MENU QUAN TRI VIEN",
                    List.of(
                            "1. Quan ly khoa hoc",
                            "2. Quan ly hoc vien",
                            "3. Quan ly dang ky khoa hoc",
                            "4. Thong ke",
                            "5. Tao tai khoan admin",
                            "0. Dang xuat"),
                    0,
                    5);
            switch (choice) {
                case 0 -> {
                    return ScreenResult.STARTUP;
                }
                case 1 -> {
                    return ScreenResult.COURSE_MENU;
                }
                case 2 -> {
                    return ScreenResult.STUDENT_MANAGEMENT_MENU;
                }
                case 3 -> {
                    return ScreenResult.ENROLLMENT_MENU;
                }
                case 4 -> {
                    return ScreenResult.STATISTICS_MENU;
                }
                case 5 -> handleCreateAdminAccount();
                default -> {
                    // Input validator already guards the range.
                }
            }
        }
    }

    private void handleCreateAdminAccount() {
        try {
            String username = promptRequiredLineOrCancel("Nhap ten dang nhap admin: ");
            if (username == null) {
                showMessagePlaceholder("TAO TAI KHOAN ADMIN", "Da huy thao tac tao tai khoan admin.");
                return;
            }
            String password = promptRequiredLineOrCancel("Nhap mat khau admin: ");
            if (password == null) {
                showMessagePlaceholder("TAO TAI KHOAN ADMIN", "Da huy thao tac tao tai khoan admin.");
                return;
            }
            String confirmPassword = promptRequiredLineOrCancel("Nhap lai mat khau: ");
            if (confirmPassword == null) {
                showMessagePlaceholder("TAO TAI KHOAN ADMIN", "Da huy thao tac tao tai khoan admin.");
                return;
            }
            if (!password.equals(confirmPassword)) {
                showMessagePlaceholder("TAO TAI KHOAN ADMIN", "Mat khau xac nhan khong khop.");
                return;
            }

            AdminAccount adminAccount = adminAccountService.createAdminAccount(username, password);
            showMessagePlaceholder(
                    "TAO TAI KHOAN ADMIN",
                    "Da tao tai khoan admin #" + adminAccount.getId() + " - " + adminAccount.getUsername() + ".");
        } catch (RuntimeException exception) {
            showMessagePlaceholder("TAO TAI KHOAN ADMIN", exception.getMessage());
        }
    }
}
