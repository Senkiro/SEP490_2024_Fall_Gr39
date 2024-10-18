package com.example.NihonStudyGuide.Mapper;

import com.example.NihonStudyGuide.dto.request.user.UserUpdateRequest;
import com.example.NihonStudyGuide.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    UserEntity toUserEntity();
    UserUpdateRequest toUserUpdateRequest(UserEntity userEntity);


}
