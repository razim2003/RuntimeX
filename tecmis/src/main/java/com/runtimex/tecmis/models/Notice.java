package com.runtimex.tecmis.models;


import java.sql.Date;

public class Notice {

    private String noticeId;
    private String adminId;
    private String title;
    private Date date;

    public Notice() {}

    public Notice(String noticeId, String adminId, String title, Date date) {
        this.noticeId = noticeId;
        this.adminId = adminId;
        this.title = title;
        this.date = date;
    }

    public String getNoticeId() { 
	return noticeId; 
	}
	
    public void setNoticeId(String noticeId) {
	this.noticeId = noticeId;
	}

    public String getAdminId() { 
	return adminId; 
	}
	
    public void setAdminId(String adminId) { 
	this.adminId = adminId;
	}

    public String getTitle() {
	return title; 
	}
	
    public void setTitle(String title) { 
	this.title = title;
	}

    public Date getDate() {
	return date;
	}
	
    public void setDate(Date date) { 
	this.date = date;
	}
} 
