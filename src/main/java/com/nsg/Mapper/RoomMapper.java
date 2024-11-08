package com.nsg.Mapper;

import com.nsg.dto.request.room.RoomRequest;
import com.nsg.dto.response.room.RoomResponse;
import com.nsg.entity.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomMapper INSTANE = Mappers.getMapper(RoomMapper.class);

    RoomResponse toRoomResponse(RoomEntity room);

    void updateRoomEntityFromRequest(RoomRequest request, @MappingTarget RoomEntity roomEntity);
}
