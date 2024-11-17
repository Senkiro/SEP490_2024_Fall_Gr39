package com.nsg.Mapper;

import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserInforUpdateRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.dto.response.student.StudentResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserUpdateRequest toUserUpdateRequest(UserEntity user);

    UserEntity toUserEntity(UserCreationRequest request);

    UserInforResponse toUserInforResponse(UserEntity userEntity);

    List<StudentResponse> toStudentListResponse(List<UserEntity> userEntities);

    UserEntity toUserEntity(UserInforUpdateRequest request);

}
