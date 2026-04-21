package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.AttendanceRecord;
import com.runtimex.tecmis.models.AttendanceSummary;
import com.runtimex.tecmis.models.CourseUnit;

import java.util.List;

public interface AttendanceDao {
    void addAttendance(AttendanceRecord record);

    void updateAttendanceStatus(String attendanceId, String status);

    List<AttendanceRecord> findAttendance(String studentId, String courseCode, String component);

    List<AttendanceSummary> getAttendanceSummaryByCourse(String courseCode, String component,
            boolean includeApprovedMedical);

    double getAttendancePercentage(String stuId, String courseCode);

    List<CourseUnit> getCoursesByStudent(String stuId);
}
