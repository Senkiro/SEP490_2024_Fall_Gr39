package com.nsg.Mapper;

import com.nsg.dto.response.holiday.HolidayResponse;
import com.nsg.entity.HolidayEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HolidayMapper {
    HolidayMapper INSTANE = Mappers.getMapper(HolidayMapper.class);

    HolidayResponse toHolidayResponse(HolidayEntity holiday);

    //void updateNewsEntityFromRequest(NewsRequest request, @MappingTarget NewEntity newEntity);
}
