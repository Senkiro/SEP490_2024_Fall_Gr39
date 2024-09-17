package com.example.NihonStudyGuide.service;

import com.example.NihonStudyGuide.dto.request.user.UserCreationRequest;
import com.example.NihonStudyGuide.dto.request.user.UserUpdateRequest;
import com.example.NihonStudyGuide.entities.UserEntity;
import java.util.List;


public interface UserService extends BaseService<UserEntity, String>  {
    List<UserEntity> getAllUser();

    void saveUser(UserEntity user);

    UserEntity userCreate(UserCreationRequest userCreationRequest);

    UserEntity getUserById(String userId);

    UserEntity updateUser(String userId, UserUpdateRequest request);

    void deleteUser(String userId);
}
