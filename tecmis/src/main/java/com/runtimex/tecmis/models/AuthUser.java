package com.runtimex.tecmis.models;

public class AuthUser {
    private final String id;
    private final String fullName;
    private final String email;
    private final String userType;

    public AuthUser(String id, String fullName, String email, String userType) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }
}