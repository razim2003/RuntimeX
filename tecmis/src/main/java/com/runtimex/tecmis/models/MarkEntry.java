package com.runtimex.tecmis.models;

public class MarkEntry {
    private String markId;
    private String studentId;
    private String courseCode;
    private String examTypeId;
    private Double mark;

    public MarkEntry() {
    }

    public MarkEntry(String markId, String studentId, String courseCode, String examTypeId, Double mark) {
        this.markId = markId;
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.examTypeId = examTypeId;
        this.mark = mark;
    }

    public String getMarkId() {
        return markId;
    }

    public void setMarkId(String markId) {
        this.markId = markId;
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

    public String getExamTypeId() {
        return examTypeId;
    }

    public void setExamTypeId(String examTypeId) {
        this.examTypeId = examTypeId;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }
}
