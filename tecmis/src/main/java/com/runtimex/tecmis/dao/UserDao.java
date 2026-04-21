package com.runtimex.tecmis.dao;

import com.runtimex.tecmis.models.AuthUser;
import com.runtimex.tecmis.models.UserProfile;

import java.util.List;

public interface UserDao {
    AuthUser authenticate(String userId, String password);

    UserProfile findById(String userId);

    List<UserProfile> findUsers(String userType, String keyword);

    void updateUserContact(String userId, String email, String contactNo);
}
