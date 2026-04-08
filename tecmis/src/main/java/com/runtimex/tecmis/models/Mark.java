package com.runtimex.tecmis.models;

public class Mark {
    private String markId;
    private String studentId;
    private String courseCode;
    private String examTypeId;
    private double mark;

    public Mark() {
    }

    public Mark(String markId, double mark, String examTypeId, String courseCode, String studentId) {
        this.markId = markId;
        this.mark = mark;
        this.examTypeId = examTypeId;
        this.courseCode = courseCode;
        this.studentId = studentId;
    }

    public String getMarkId() {
        return markId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getExamTypeId() {
        return examTypeId;
    }

    public double getMark() {
        return mark;
    }

    public void setMarkId(String markId) {
        this.markId = markId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setExamTypeId(String examTypeId) {
        this.examTypeId = examTypeId;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }
}
