package com.nsg.Mapper;

import com.nsg.dto.request.news.NewsRequest;
import com.nsg.dto.request.room.RoomRequest;
import com.nsg.dto.response.news.NewsResponse;
import com.nsg.dto.response.room.RoomResponse;
import com.nsg.entity.NewEntity;
import com.nsg.entity.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsMapper INSTANE = Mappers.getMapper(NewsMapper.class);

    NewsResponse toNewsResponse(NewEntity news);

    void updateNewsEntityFromRequest(NewsRequest request, @MappingTarget NewEntity newEntity);
}
