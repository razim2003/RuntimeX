package com.runtimex.tecmis.models;

public class EnrollmentRecord {

    private String studentId;
    private String studentName;
    private String courseCode;
    private String courseTitle;

    public EnrollmentRecord() {
    }

    public EnrollmentRecord(String studentId, String studentName, String courseCode, String courseTitle) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
}
