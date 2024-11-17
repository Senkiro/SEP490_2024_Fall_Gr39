package com.nsg.Mapper;

import com.nsg.dto.request.exam.ExamTypeRequest;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.dto.response.exam.ExamTypeResponse;
import com.nsg.entity.ExamTypeRateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExamTypeMapper {

    ExamTypeMapper INSTANCE = Mappers.getMapper(ExamTypeMapper.class);

    ExamTypeResponse toExamTypeResponse(ExamTypeRateEntity examTypeRateEntity);

    ExamTypeRateEntity toExamTypeRateEntity(ExamTypeResponse response);

    void updateExamTypeRateFromRequest(ExamTypeRequest request, @MappingTarget ExamTypeRateEntity examTypeRate);

}
