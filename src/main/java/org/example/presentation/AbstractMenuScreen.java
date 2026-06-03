package org.example.presentation;

import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

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

    protected void showListPlaceholder(String title, List<String> headers, String note) {
        featureScreen.showTable(title, headers, note);
    }

    protected void showMessagePlaceholder(String title, String note) {
        featureScreen.showMessage(title, note);
    }
}
