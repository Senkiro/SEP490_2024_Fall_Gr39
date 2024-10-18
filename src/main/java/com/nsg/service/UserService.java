package com.nsg.service;

import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.entity.UserEntity;
import java.util.List;


public interface UserService extends BaseService<UserEntity, String>  {
    List<UserEntity> getAllUser();

    void saveUser(UserEntity user);

    UserEntity userCreate(UserCreationRequest userCreationRequest);

    UserEntity getUserById(String userId);

    UserEntity getUserByEmail(String email);

    UserEntity updateUser(String userId, UserUpdateRequest request);

    String resetPassword(UserEntity user, String newpass);

    String forgetPassword(UserEntity user, String toEmail);

    void deleteUser(String userId);
}
