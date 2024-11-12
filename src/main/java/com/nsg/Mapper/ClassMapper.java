package com.nsg.Mapper;

import com.nsg.dto.response.classResponse.ClassResponse;
import com.nsg.entity.ClassEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClassMapper {
    ClassMapper INSTANCE = Mappers.getMapper(ClassMapper.class);

    ClassResponse toClassResponse(ClassEntity classEntity);

}
