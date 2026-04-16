package com.runtimex.tecmis.models;

public class ExamMedical {
    private String refNo;
    private String studentId;
    private CourseExam courseExam;
    private String status;
    private String submittedDate;

    public ExamMedical() {}

    public ExamMedical(String refNo, String studentId, CourseExam courseExam, String status, String submittedDate) {
        this.refNo = refNo;
        this.studentId = studentId;
        this.courseExam = courseExam;
        this.status = status;
        this.submittedDate = submittedDate;
    }

    public String getRefNo() { return refNo; }
    public String getStudentId() { return studentId; }
    public CourseExam getCourseExam() { return courseExam; }
    public String getStatus() { return status; }
    public String getSubmittedDate() { return submittedDate; }

    public void setRefNo(String refNo) { this.refNo = refNo; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setCourseExam(CourseExam courseExam) { this.courseExam = courseExam; }
    public void setStatus(String status) { this.status = status; }
    public void setSubmittedDate(String submittedDate) { this.submittedDate = submittedDate; }
}