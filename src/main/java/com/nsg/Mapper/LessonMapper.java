package com.nsg.Mapper;

import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.entity.LessonEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    LessonEntity toLessonEntity(LessonCreateRequest lessonCreateRequest);
}
