package com.nsg.service;

import com.nsg.common.enums.UserRole;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.ResetPasswordRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserInforUpdateRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.StudentEntity;
import com.nsg.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserService extends BaseService<UserEntity, String>  {
    List<UserEntity> getAllUser();

    void saveUser(UserEntity user);

    UserEntity createUser(UserCreationRequest userCreationRequest, UserRole role);

    UserEntity getUserById(String userId);

    UserEntity getUserByEmail(String email);

    UserEntity updateUserPass(String userId, UserUpdateRequest request);

    UserInforResponse getUserInforById(String userId);

    UserInforResponse updateUserInfor(String userId, UserInforUpdateRequest request, MultipartFile avatar);

    UserInforResponse updateUserPassword(ResetPasswordRequest request);

    String resetPassword(UserEntity user, String newpass);

    String forgetPassword(UserEntity user, String toEmail);

    void deleteUser(String userId);

    Page<UserInforResponse> getUsersByRoles(UserRole role, int page, int size);

    void changeUsersActive(String userId);

}
