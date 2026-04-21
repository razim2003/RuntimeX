package com.runtimex.tecmis.services.impl;

import com.runtimex.tecmis.dao.AttendanceDao;
import com.runtimex.tecmis.models.AttendanceSummary;
import com.runtimex.tecmis.services.interfaces.AttendanceService;

import java.util.List;

public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceDao attendanceDao;

    public AttendanceServiceImpl(AttendanceDao attendanceDao) {
        this.attendanceDao = attendanceDao;
    }

    @Override
    public double getAttendancePercentage(String stuId, String courseCode) {
        return attendanceDao.getAttendancePercentage(stuId, courseCode);
    }

    public List<AttendanceSummary> getSummaryForCourse(String courseCode, String component,
            boolean includeApprovedMedical) {
        return attendanceDao.getAttendanceSummaryByCourse(courseCode, component, includeApprovedMedical);
    }
}