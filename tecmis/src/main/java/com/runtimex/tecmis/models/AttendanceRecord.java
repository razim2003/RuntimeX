package com.runtimex.tecmis.models;

public class AttendanceRecord {
    private String attendanceId;
    private String studentId;
    private String courseCode;
    private String sessionDate;
    private String component;
    private String status;
    private String medicalRefNo;
    private String medicalStatus;

    public AttendanceRecord() {
    }

    public AttendanceRecord(String attendanceId, String studentId, String courseCode, String sessionDate,
            String component, String status) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.sessionDate = sessionDate;
        this.component = component;
        this.status = status;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMedicalRefNo() {
        return medicalRefNo;
    }

    public void setMedicalRefNo(String medicalRefNo) {
        this.medicalRefNo = medicalRefNo;
    }

    public String getMedicalStatus() {
        return medicalStatus;
    }

    public void setMedicalStatus(String medicalStatus) {
        this.medicalStatus = medicalStatus;
    }
}
