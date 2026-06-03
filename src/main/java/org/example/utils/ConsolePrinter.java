package org.example.utils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class ConsolePrinter {
    private final PrintStream out;

    public ConsolePrinter(PrintStream out) {
        this.out = Objects.requireNonNull(out, "out");
    }

    public void printBanner(String title) {
        out.println();
        out.println(title);
        out.println(repeat('=', Math.max(24, title.length())));
        out.println();
    }

    public void printMenu(List<String> items) {
        for (String item : items) {
            out.println(item);
        }
        out.println();
    }

    public void printMessage(String message) {
        out.println(message);
        out.println();
    }

    public void printTable(String title, List<String> headers, List<List<String>> rows) {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(headers, "headers");
        Objects.requireNonNull(rows, "rows");
        if (headers.isEmpty()) {
            throw new IllegalArgumentException("Headers must not be empty.");
        }

        List<List<String>> contentRows = new ArrayList<>(rows);
        if (contentRows.isEmpty()) {
            List<String> placeholder = new ArrayList<>(Collections.nCopies(headers.size(), ""));
            placeholder.set(0, "Chua co du lieu");
            contentRows.add(placeholder);
        }

        int[] widths = calculateWidths(headers, contentRows);
        String border = buildBorder(widths);

        out.println();
        out.println(title);
        out.println(border);
        out.println(buildRow(headers, widths));
        out.println(border);
        for (List<String> row : contentRows) {
            out.println(buildRow(row, widths));
        }
        out.println(border);
        out.println();
    }

    private int[] calculateWidths(List<String> headers, List<List<String>> rows) {
        int[] widths = new int[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            widths[i] = headers.get(i).length();
        }
        for (List<String> row : rows) {
            for (int i = 0; i < headers.size(); i++) {
                String cell = i < row.size() ? row.get(i) : "";
                widths[i] = Math.max(widths[i], cell.length());
            }
        }
        return widths;
    }

    private String buildBorder(int[] widths) {
        StringBuilder builder = new StringBuilder("+");
        for (int width : widths) {
            builder.append(repeat('-', width + 2)).append("+");
        }
        return builder.toString();
    }

    private String buildRow(List<String> values, int[] widths) {
        StringBuilder builder = new StringBuilder("|");
        for (int i = 0; i < widths.length; i++) {
            String value = i < values.size() ? values.get(i) : "";
            builder.append(' ')
                    .append(padRight(value, widths[i]))
                    .append(' ')
                    .append('|');
        }
        return builder.toString();
    }

    private String padRight(String value, int width) {
        String text = value == null ? "" : value;
        if (text.length() >= width) {
            return text;
        }
        return String.format(Locale.ROOT, "%-" + width + "s", text);
    }

    private String repeat(char ch, int count) {
        StringBuilder builder = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            builder.append(ch);
        }
        return builder.toString();
    }
}
