package com.nsg.Mapper;

import com.nsg.dto.request.lesson.LessonCreateRequest;
import com.nsg.dto.response.lesson.LessonResponse;
import com.nsg.entity.LessonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    LessonEntity toLessonEntity(LessonCreateRequest lessonCreateRequest);

    LessonResponse toLessonResponse(LessonEntity lesson);
}
