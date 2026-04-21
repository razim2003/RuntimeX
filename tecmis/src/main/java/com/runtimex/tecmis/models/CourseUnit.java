package com.runtimex.tecmis.models;

public class CourseUnit {

    private String courseCode;
    private String title;
    private int credit;


    public CourseUnit() {
    }

    public CourseUnit(String courseCode, String title, int credit) {
        this.courseCode = courseCode;
        this.title = title;
        this.credit = credit;
    }


    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
