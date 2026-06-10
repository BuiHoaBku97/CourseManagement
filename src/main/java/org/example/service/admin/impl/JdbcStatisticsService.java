package org.example.service.admin.impl;

import org.example.dao.IStatisticsDao;
import org.example.entity.CourseEnrollmentStat;
import org.example.entity.SystemSummary;
import org.example.service.admin.StatisticsService;

import java.util.List;
import java.util.Objects;

public final class JdbcStatisticsService implements StatisticsService {
    private final IStatisticsDao statisticsDao;

    public JdbcStatisticsService(IStatisticsDao statisticsDao) {
        this.statisticsDao = Objects.requireNonNull(statisticsDao, "statisticsDao");
    }

    @Override
    public SystemSummary getSummary() {
        return statisticsDao.getSummary();
    }

    @Override
    public List<CourseEnrollmentStat> getStudentCountByCourse() {
        return statisticsDao.getStudentCountByCourse();
    }

    @Override
    public List<CourseEnrollmentStat> getTop5CoursesByEnrollment() {
        return statisticsDao.getTop5CoursesByEnrollment();
    }

    @Override
    public List<CourseEnrollmentStat> getCoursesWithMoreThan10Students() {
        return statisticsDao.getCoursesWithMoreThan10Students();
    }
}
