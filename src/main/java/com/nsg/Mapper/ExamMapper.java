package com.nsg.Mapper;

import com.nsg.dto.request.exam.ExamRequest;
import com.nsg.dto.response.exam.ExamResponse;
import com.nsg.entity.ExamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExamMapper {

    ExamMapper INSTANCE = Mappers.getMapper(ExamMapper.class);

    ExamEntity toExamEntity(ExamRequest request);

    ExamResponse toExamResponse(ExamEntity exam);

}
