package com.runtimex.tecmis.models;

public class CourseResultSummary {
    private String studentId;
    private String studentName;
    private String courseCode;
    private double attendancePercentage;
    private String eligibilityStatus;
    private double caPercentage;
    private String caStatus;
    private double endPercentage;
    private String endStatus;
    private double totalMark;
    private String grade;
    private boolean medicalConcession;
    private String studentStatus;

    public CourseResultSummary() {
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

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public String getEligibilityStatus() {
        return eligibilityStatus;
    }

    public void setEligibilityStatus(String eligibilityStatus) {
        this.eligibilityStatus = eligibilityStatus;
    }

    public double getCaPercentage() {
        return caPercentage;
    }

    public void setCaPercentage(double caPercentage) {
        this.caPercentage = caPercentage;
    }

    public String getCaStatus() {
        return caStatus;
    }

    public void setCaStatus(String caStatus) {
        this.caStatus = caStatus;
    }

    public double getEndPercentage() {
        return endPercentage;
    }

    public void setEndPercentage(double endPercentage) {
        this.endPercentage = endPercentage;
    }

    public String getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(String endStatus) {
        this.endStatus = endStatus;
    }

    public double getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(double totalMark) {
        this.totalMark = totalMark;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public boolean isMedicalConcession() {
        return medicalConcession;
    }

    public void setMedicalConcession(boolean medicalConcession) {
        this.medicalConcession = medicalConcession;
    }

    public String getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }
}
