package com.nsg.service;

import com.nsg.common.enums.UserRole;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserInforUpdateRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.dto.response.staff.StudentResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.StudentEntity;
import com.nsg.entity.UserEntity;
import java.util.List;


public interface UserService extends BaseService<UserEntity, String>  {
    List<UserEntity> getAllUser();

    void saveUser(UserEntity user);

    UserEntity userCreate(UserCreationRequest userCreationRequest, UserRole role);

    UserEntity getUserById(String userId);

    UserEntity getUserByEmail(String email);

    UserEntity updateUserPass(String userId, UserUpdateRequest request);

    UserInforResponse getUserInforById(String userId);

    UserInforResponse updateUserInfor(String userId, UserInforUpdateRequest request);

    String resetPassword(UserEntity user, String newpass);

    String forgetPassword(UserEntity user, String toEmail);

    void deleteUser(String userId);

    List<UserEntity> getUserByRoles(UserRole role);

    List<StudentResponse> getAllStudent();

    StudentEntity studentCreate(StudentCreattionRequest request);
}
