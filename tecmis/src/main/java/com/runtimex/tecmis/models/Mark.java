package com.runtimex.tecmis.models;

public class Mark {
    private String markId;
    private String studentId;
    private CourseExam courseExam;
    private double mark;

    public Mark() {}

    public Mark(String markId, String studentId, CourseExam courseExam, double mark) {
        this.markId = markId;
        this.studentId = studentId;
        this.courseExam = courseExam;
        this.mark = mark;
    }

    public String getMarkId() { return markId; }
    public String getStudentId() { return studentId; }
    public CourseExam getCourseExam() { return courseExam; }
    public double getMark() { return mark; }

    public void setMarkId(String markId) { this.markId = markId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setCourseExam(CourseExam courseExam) { this.courseExam = courseExam; }
    public void setMark(double mark) { this.mark = mark; }
}