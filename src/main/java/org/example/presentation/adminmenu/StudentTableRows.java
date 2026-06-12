package org.example.presentation.adminmenu;

import org.example.entity.Student;

import java.util.List;

final class StudentTableRows {
    static final List<String> HEADERS =
            List.of("ID", "Ho ten", "Ngay sinh", "Gioi tinh", "Email", "So dien thoai", "Ngay tao");

    private StudentTableRows() {
    }

    static List<String> toRow(Student student) {
        return List.of(
                String.valueOf(student.getId()),
                student.getName(),
                student.getDob().toString(),
                student.isSex() ? "Nam" : "Nu",
                student.getEmail(),
                student.getPhone() == null ? "" : student.getPhone(),
                student.getCreatedAt().toString());
    }
}
