package org.example.utils;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

public final class ConsoleInput {
    private final Scanner scanner;
    private final PrintStream out;

    public ConsoleInput(InputStream inputStream, PrintStream printStream) {
        this.scanner = new Scanner(Objects.requireNonNull(inputStream, "inputStream"));
        this.out = Objects.requireNonNull(printStream, "printStream");
    }

    public String readRequiredLine(String prompt) {
        while (true) {
            out.print(prompt);
            out.flush();
            String value = scanner.nextLine();
            if (!InputValidator.isBlank(value)) {
                return value.trim();
            }
            out.println("Gia tri khong duoc de trong.");
        }
    }

    public int readChoice(String prompt, int minInclusive, int maxInclusive) {
        while (true) {
            out.print(prompt);
            out.flush();
            Integer choice = InputValidator.parseInteger(scanner.nextLine());
            if (choice != null && InputValidator.isInRange(choice, minInclusive, maxInclusive)) {
                return choice;
            }
            out.println(
                    "Lua chon khong hop le. Vui long nhap so trong khoang "
                            + minInclusive
                            + " den "
                            + maxInclusive
                            + ".");
        }
    }

    public void pause(String prompt) {
        out.print(prompt);
        out.flush();
        scanner.nextLine();
    }
}
