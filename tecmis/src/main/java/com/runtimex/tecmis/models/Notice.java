package com.runtimex.tecmis.models;

import java.sql.Date;

public class Notice {

    private String noticeId;
    private String adminId;
    private String title;
    private Date   date;
    private String filePath;   // absolute path on disk, nullable
    private String fileType;   // "pdf" | "png" | null

    public Notice() {}

    public Notice(String noticeId, String adminId, String title, Date date) {
        this.noticeId = noticeId;
        this.adminId  = adminId;
        this.title    = title;
        this.date     = date;
    }

    public Notice(String noticeId, String adminId, String title, Date date,
                  String filePath, String fileType) {
        this.noticeId = noticeId;
        this.adminId  = adminId;
        this.title    = title;
        this.date     = date;
        this.filePath = filePath;
        this.fileType = fileType;
    }

    public String getNoticeId()               { return noticeId; }
    public void   setNoticeId(String v)       { this.noticeId = v; }

    public String getAdminId()                { return adminId; }
    public void   setAdminId(String v)        { this.adminId = v; }

    public String getTitle()                  { return title; }
    public void   setTitle(String v)          { this.title = v; }

    public Date   getDate()                   { return date; }
    public void   setDate(Date v)             { this.date = v; }

    public String getFilePath()               { return filePath; }
    public void   setFilePath(String v)       { this.filePath = v; }

    public String getFileType()               { return fileType; }
    public void   setFileType(String v)       { this.fileType = v; }

    /** Returns true when this notice has an attached file. */
    public boolean hasFile() {
        return filePath != null && !filePath.isBlank();
    }
} 
