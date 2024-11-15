package com.nsg.Mapper;

import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.dto.response.event.EventResponse;
import com.nsg.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventResponse toEventResponse(EventEntity entity);

    EventEntity toEventEntity(EventCreateRequest eventCreateRequest);
}
