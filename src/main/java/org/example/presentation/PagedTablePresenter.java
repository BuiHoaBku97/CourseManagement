package org.example.presentation;

import org.example.common.Page;
import org.example.common.PageRequest;
import org.example.utils.ConsoleInput;
import org.example.utils.ConsolePrinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class PagedTablePresenter {
    private final ConsoleInput input;
    private final ConsolePrinter printer;

    public PagedTablePresenter(ConsoleInput input, ConsolePrinter printer) {
        this.input = Objects.requireNonNull(input, "input");
        this.printer = Objects.requireNonNull(printer, "printer");
    }

    public <T> void show(
            String title,
            List<String> headers,
            String emptyMessage,
            Function<PageRequest, Page<T>> pageLoader,
            Function<T, List<String>> rowMapper) {
        PageRequest currentRequest = PageRequest.firstPage();
        while (true) {
            Page<T> page = pageLoader.apply(currentRequest);
            List<List<String>> rows = new ArrayList<>();
            for (T item : page.getContent()) {
                rows.add(rowMapper.apply(item));
            }
            if (rows.isEmpty()) {
                List<String> placeholder = new ArrayList<>(Collections.nCopies(headers.size(), ""));
                placeholder.set(0, emptyMessage);
                rows.add(placeholder);
            }

            printer.printTable(title, headers, rows);
            printer.printMessage(
                    "Trang " + page.getPage() + "/" + page.totalPages() + " (tong " + page.getTotalElements() + " ban ghi)");

            if (page.totalPages() <= 1) {
                input.pause("Nhan Enter de quay lai menu...");
                return;
            }

            int choice =
                    input.readChoice(
                            "1. Trang tiep theo\n2. Trang truoc\n0. Quay lai menu\nLua chon: ",
                            0,
                            2);
            if (choice == 0) {
                return;
            }
            if (choice == 1) {
                if (!page.hasNext()) {
                    printer.printMessage("Ban dang o trang cuoi.");
                    continue;
                }
                currentRequest = currentRequest.next();
                continue;
            }
            if (!page.hasPrevious()) {
                printer.printMessage("Ban dang o trang dau.");
                continue;
            }
            currentRequest = currentRequest.previous();
        }
    }
}
