package com.runtimex.tecmis.models;

public class UserProfile {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNo;
    private String profileImagePath;
    private String userType;
    private String status;

    public UserProfile() {
    }

    public UserProfile(String id, String firstName, String lastName, String email, String contactNo, String userType,
            String status) {
        this(id, firstName, lastName, email, contactNo, null, userType, status);
    }

    public UserProfile(String id, String firstName, String lastName, String email, String contactNo,
            String profileImagePath, String userType, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactNo = contactNo;
        this.profileImagePath = profileImagePath;
        this.userType = userType;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getUserType() {
        return userType;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}