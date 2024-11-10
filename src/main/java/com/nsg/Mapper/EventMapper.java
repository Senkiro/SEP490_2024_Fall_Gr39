package com.nsg.Mapper;

import com.nsg.dto.request.event.EventCreateRequest;
import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.entity.EventEntity;
import com.nsg.entity.LessonEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventEntity toEventEntity(EventCreateRequest eventCreateRequest);
}
