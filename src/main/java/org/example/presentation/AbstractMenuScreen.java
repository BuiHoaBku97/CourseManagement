package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;
import org.example.utils.InputValidator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

public abstract class AbstractMenuScreen implements Screen {
    protected final ConsoleInput input;
    protected final ConsolePrinter printer;
    protected final FeatureScreen featureScreen;

    protected AbstractMenuScreen(ConsoleInput input, ConsolePrinter printer) {
        this.input = Objects.requireNonNull(input, "input");
        this.printer = Objects.requireNonNull(printer, "printer");
        this.featureScreen = new FeatureScreen(input, printer);
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

    protected void showListPlaceholder(String title, List<String> headers, String note) {
        featureScreen.showTable(title, headers, note);
    }

    protected void showMessagePlaceholder(String title, String note) {
        featureScreen.showMessage(title, note);
    }

    protected void showTable(String title, List<String> headers, List<List<String>> rows) {
        printer.printTable(title, headers, rows);
        input.pause("Nhan Enter de quay lai menu...");
    }

    protected int promptPositiveInt(String prompt) {
        while (true) {
            Integer value = InputValidator.parseInteger(input.readLine(prompt));
            if (value != null && value > 0) {
                return value;
            }
            printer.printMessage("Vui long nhap so nguyen duong hop le.");
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
