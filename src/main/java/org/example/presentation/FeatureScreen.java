package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.List;
import java.util.Objects;

public final class FeatureScreen {
    private final ConsoleInput input;
    private final ConsolePrinter printer;

    public FeatureScreen(ConsoleInput input, ConsolePrinter printer) {
        this.input = Objects.requireNonNull(input, "input");
        this.printer = Objects.requireNonNull(printer, "printer");
    }

    public void showMessage(String title, String note) {
        printer.printBanner(title);
        printer.printMessage(note);
        input.pause("Nhan Enter de quay lai menu...");
    }

    public void showTable(String title, List<String> headers, String note) {
        printer.printTable(title, headers, List.of());
        printer.printMessage(note);
        input.pause("Nhan Enter de quay lai menu...");
    }
}
