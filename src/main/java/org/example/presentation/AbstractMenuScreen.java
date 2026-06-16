package org.example.presentation;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;
import org.example.utils.InputValidator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class AbstractMenuScreen implements Screen {
    protected final ConsoleInput input;
    protected final ConsolePrinter printer;
    protected final FeatureScreen featureScreen;
    private final PagedTablePresenter pagedTablePresenter;

    protected AbstractMenuScreen(ConsoleInput input, ConsolePrinter printer) {
        this.input = Objects.requireNonNull(input, "input");
        this.printer = Objects.requireNonNull(printer, "printer");
        this.featureScreen = new FeatureScreen(input, printer);
        this.pagedTablePresenter = new PagedTablePresenter(input, printer);
    }

    protected void renderMenu(String title, List<String> options) {
        printer.printBanner(title);
        printer.printMenu(options);
    }

    protected int promptChoice(String title, List<String> options, int minInclusive, int maxInclusive) {
        while (true) {
            renderMenu(title, options);
            Integer choice = InputValidator.parseInteger(input.readLine("Lua chon: "));
            if (choice != null && InputValidator.isInRange(choice, minInclusive, maxInclusive)) {
                return choice;
            }
            printer.printMessage(
                    "Lua chon khong hop le. Vui long nhap so trong khoang "
                            + minInclusive
                            + " den "
                            + maxInclusive
                            + ".");
        }
    }

    protected void showMessagePlaceholder(String title, String note) {
        featureScreen.showMessage(title, note);
    }

    protected void showTable(String title, List<String> headers, List<List<String>> rows) {
        printer.printTable(title, headers, rows);
        input.pause("Nhan Enter de quay lai menu...");
    }

    protected <T> void showPagedTable(
            String title,
            List<String> headers,
            String emptyMessage,
            Function<PageRequest, Page<T>> pageLoader,
            Function<T, List<String>> rowMapper) {
        pagedTablePresenter.show(title, headers, emptyMessage, pageLoader, rowMapper);
    }

    protected Integer promptPositiveIntOrCancel(String prompt) {
        while (true) {
            String value = input.readLine(prompt);
            Integer parsed = InputValidator.parseInteger(value);
            if (parsed != null) {
                if (parsed == -1) {
                    return null;
                }
                if (parsed > 0) {
                    return parsed;
                }
            }
            printer.printMessage("Vui long nhap so nguyen duong hop le hoac -1 de huy.");
        }
    }

    protected LocalDate promptDate(String prompt) {
        while (true) {
            String value = input.readRequiredLine(prompt);
            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException exception) {
                printer.printMessage("Ngay khong hop le. Dinh dang mong doi: yyyy-MM-dd.");
            }
        }
    }

    protected LocalDate promptDateOrCancel(String prompt) {
        while (true) {
            String value = input.readLine(prompt);
            if ("-1".equals(value == null ? null : value.trim())) {
                return null;
            }
            try {
                return LocalDate.parse(value.trim());
            } catch (DateTimeParseException exception) {
                printer.printMessage("Ngay khong hop le. Dinh dang mong doi: yyyy-MM-dd. Nhap -1 de huy.");
            }
        }
    }

    protected String promptRequiredLineOrCancel(String prompt) {
        while (true) {
            String value = input.readLine(prompt);
            if ("-1".equals(value == null ? null : value.trim())) {
                return null;
            }
            if (!InputValidator.isBlank(value)) {
                return value.trim();
            }
            printer.printMessage("Gia tri khong duoc de trong. Nhap -1 de huy.");
        }
    }

    protected String promptEmailOrCancel(String prompt) {
        while (true) {
            String value = input.readLine(prompt);
            if ("-1".equals(value == null ? null : value.trim())) {
                return null;
            }
            String normalized = value == null ? "" : value.trim();
            if (InputValidator.isValidEmail(normalized)) {
                return normalized;
            }
            printer.printMessage(
                    "Email khong hop le. Dinh dang: chi cho phep chu cai, chu so, '_' , '.' va ket thuc bang @gmail.com. Nhap -1 de huy.");
        }
    }

    protected String promptLineOrCancel(String prompt) {
        String value = input.readLine(prompt);
        if ("-1".equals(value == null ? null : value.trim())) {
            return null;
        }
        return value == null ? null : value.trim();
    }

    protected String promptPhoneOrCancel(String prompt) {
        while (true) {
            String value = input.readLine(prompt);
            if ("-1".equals(value == null ? null : value.trim())) {
                return null;
            }
            String normalized = value == null ? "" : value.trim();
            if (InputValidator.isBlank(normalized) || InputValidator.isValidPhone(normalized)) {
                return normalized;
            }
            printer.printMessage(
                    "So dien thoai khong hop le. Co the de trong hoac phai gom 10 chu so va bat dau bang 0. Nhap -1 de huy.");
        }
    }

    protected Boolean promptYesNoOrCancel(String title, String yesLabel, String noLabel) {
        return promptYesNoOrCancel(title, yesLabel, noLabel, true);
    }

    protected Boolean promptYesNoOrCancel(String title, String yesLabel, String noLabel, boolean showCancelOption) {
        while (true) {
            List<String> options = showCancelOption
                    ? List.of("1. " + yesLabel, "0. " + noLabel, "-1. Huy")
                    : List.of("1. " + yesLabel, "0. " + noLabel);
            renderMenu(title, options);
            Integer choice = InputValidator.parseInteger(input.readLine("Lua chon: "));
            if (choice == null) {
                printer.printMessage(
                        showCancelOption
                                ? "Lua chon khong hop le. Vui long nhap 1, 0 hoac -1 de huy."
                                : "Lua chon khong hop le. Vui long nhap 1 hoac 0.");
                continue;
            }
            if (choice == -1) {
                if (!showCancelOption) {
                    return null;
                }
                return null;
            }
            if (choice == 1) {
                return true;
            }
            if (choice == 0) {
                return false;
            }
            printer.printMessage(
                    showCancelOption
                            ? "Lua chon khong hop le. Vui long nhap 1, 0 hoac -1 de huy."
                            : "Lua chon khong hop le. Vui long nhap 1 hoac 0.");
        }
    }

    protected boolean promptYesNo(String title, String yesLabel, String noLabel) {
        return promptChoice(title, List.of("1. " + yesLabel, "0. " + noLabel), 0, 1) == 1;
    }

    protected boolean confirmAction(String title) {
        renderMenu(
                title,
                List.of(
                        "1. Xac nhan",
                        "0. Huy"));
        return input.readChoice("Lua chon: ", 0, 1) == 1;
    }
}
