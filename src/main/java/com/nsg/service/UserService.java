package com.nsg.service;

import com.nsg.common.enums.UserRole;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.dto.response.staff.StudentResponse;
import com.nsg.entity.StudentEntity;
import com.nsg.entity.UserEntity;
import java.util.List;


public interface UserService extends BaseService<UserEntity, String>  {
    List<UserEntity> getAllUser();

    void saveUser(UserEntity user);

    UserEntity userCreate(UserCreationRequest userCreationRequest, UserRole role);

    UserEntity getUserById(String userId);

    UserEntity getUserByEmail(String email);

    UserEntity updateUser(String userId, UserUpdateRequest request);

    String resetPassword(UserEntity user, String newpass);

    String forgetPassword(UserEntity user, String toEmail);

    void deleteUser(String userId);

    List<UserEntity> getUserByRoles(UserRole role);

    List<StudentResponse> getAllStudent();

    StudentEntity studentCreate(StudentCreattionRequest request);
}
