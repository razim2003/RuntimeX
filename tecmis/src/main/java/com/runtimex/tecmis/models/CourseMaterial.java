package com.runtimex.tecmis.models;

public class CourseMaterial {

    private String materialId;
    private String courseCode;
    private String lecId;
    private String title;
    private String description;
    private String materialType; // File | Link | Text
    private String content;      // file path, URL, or raw text
    private String uploadedDate;

    public CourseMaterial() {}

    public CourseMaterial(String materialId, String courseCode, String lecId,
                          String title, String description,
                          String materialType, String content, String uploadedDate) {
        this.materialId   = materialId;
        this.courseCode   = courseCode;
        this.lecId        = lecId;
        this.title        = title;
        this.description  = description;
        this.materialType = materialType;
        this.content      = content;
        this.uploadedDate = uploadedDate;
    }

    // ── getters & setters ──────────────────────────────────────────────

    public String getMaterialId()             { return materialId; }
    public void   setMaterialId(String v)     { this.materialId = v; }

    public String getCourseCode()             { return courseCode; }
    public void   setCourseCode(String v)     { this.courseCode = v; }

    public String getLecId()                  { return lecId; }
    public void   setLecId(String v)          { this.lecId = v; }

    public String getTitle()                  { return title; }
    public void   setTitle(String v)          { this.title = v; }

    public String getDescription()            { return description; }
    public void   setDescription(String v)    { this.description = v; }

    public String getMaterialType()           { return materialType; }
    public void   setMaterialType(String v)   { this.materialType = v; }

    public String getContent()                { return content; }
    public void   setContent(String v)        { this.content = v; }

    public String getUploadedDate()           { return uploadedDate; }
    public void   setUploadedDate(String v)   { this.uploadedDate = v; }
}
