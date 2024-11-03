package com.nsg.Mapper;

import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    UserEntity toUserEntity();

    UserUpdateRequest toUserUpdateRequest(UserEntity user);

    UserEntity toUserEntity(UserCreationRequest request);

}
