package com.runtimex.tecmis.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {

    private String eventId;
    private String userId;
    private String title;
    private String description;
    private LocalDate date;
    private LocalTime time;


    public Event() {
    }


    public Event(String eventId, String userId, String title, String description, LocalDate date, LocalTime time) {
        this.eventId = eventId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }



    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}