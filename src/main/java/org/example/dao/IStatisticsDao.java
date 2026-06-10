package org.example.dao;

import org.example.entity.CourseEnrollmentStat;
import org.example.entity.SystemSummary;

import java.util.List;

public interface IStatisticsDao {
    SystemSummary getSummary();

    List<CourseEnrollmentStat> getStudentCountByCourse();

    List<CourseEnrollmentStat> getTop5CoursesByEnrollment();

    List<CourseEnrollmentStat> getCoursesWithMoreThan10Students();
}
