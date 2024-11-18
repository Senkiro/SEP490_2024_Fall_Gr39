package com.nsg.Mapper;

import com.nsg.dto.response.session.SessionResponse;
import com.nsg.entity.SessionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    SessionResponse toSessionResponse(SessionEntity sessionEntity);
}
