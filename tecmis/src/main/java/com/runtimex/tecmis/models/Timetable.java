package com.runtimex.tecmis.models;

public class Timetable {

    private String timetableId;
    private String adminId;
    private String lecturerId;
    private String courseCode;
    private String location;
    private int    level;
    private String type;       // Theory | Practical
    private int    hours;
    private String dayOfWeek;  // Monday … Sunday  (NEW)
    private String startTime;  // HH:mm             (NEW)

    public Timetable() {}

    // ── getters & setters ──────────────────────────────────────────────

    public String getTimetableId()            { return timetableId; }
    public void   setTimetableId(String v)    { this.timetableId = v; }

    public String getAdminId()                { return adminId; }
    public void   setAdminId(String v)        { this.adminId = v; }

    public String getLecturerId()             { return lecturerId; }
    public void   setLecturerId(String v)     { this.lecturerId = v; }

    public String getCourseCode()             { return courseCode; }
    public void   setCourseCode(String v)     { this.courseCode = v; }

    public String getLocation()               { return location; }
    public void   setLocation(String v)       { this.location = v; }

    public int    getLevel()                  { return level; }
    public void   setLevel(int v)             { this.level = v; }

    public String getType()                   { return type; }
    public void   setType(String v)           { this.type = v; }

    public int    getHours()                  { return hours; }
    public void   setHours(int v)             { this.hours = v; }

    public String getDayOfWeek()              { return dayOfWeek; }
    public void   setDayOfWeek(String v)      { this.dayOfWeek = v; }

    public String getStartTime()              { return startTime; }
    public void   setStartTime(String v)      { this.startTime = v; }
}
