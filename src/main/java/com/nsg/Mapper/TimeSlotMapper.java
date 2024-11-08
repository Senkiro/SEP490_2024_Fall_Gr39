package com.nsg.Mapper;

import com.nsg.dto.request.timeSlot.TimeSlotUpdateRequest;
import com.nsg.dto.response.timeSlot.TimeSlotResponse;
import com.nsg.entity.TimeSlotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeSlotMapper {
    TimeSlotMapper INSTANCE = Mappers.getMapper(TimeSlotMapper.class);

//    List<TimeSlotResponse> toTimeSlotResponse(List<TimeSlotEntity> timeSlotEntityList);
    TimeSlotResponse toTimeSlotResponse(TimeSlotEntity timeSlotEntity);

    void updateTimeSlotFromRequest(TimeSlotUpdateRequest request, @MappingTarget TimeSlotEntity timeSlot);
}
