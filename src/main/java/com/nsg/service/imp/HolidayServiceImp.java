package com.nsg.service.imp;

import com.nsg.Mapper.HolidayMapper;
import com.nsg.dto.request.holiday.HolidayRequest;
import com.nsg.dto.response.holiday.HolidayResponse;
import com.nsg.entity.HolidayEntity;
import com.nsg.repository.HolidayRepository;
import com.nsg.service.HolidayService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class HolidayServiceImp implements HolidayService {
    @Autowired
    HolidayRepository holidayRepository;

    @Override
    public HolidayResponse createHoliday(HolidayRequest request) {
        HolidayEntity holiday = new HolidayEntity();

        //map
        BeanUtils.copyProperties(request, holiday);

        return HolidayMapper.INSTANE.toHolidayResponse(holidayRepository.save(holiday));
    }

    @Override
    public Page<HolidayResponse> getAllHoliday(int page, int size) {
        Page<HolidayEntity> holidayEntitiesList = holidayRepository.findAll(PageRequest.of(page, size));

        List<HolidayResponse> responseList = holidayEntitiesList.stream()
                .map(holidayEntity -> new HolidayResponse(
                        holidayEntity.getHolidayId(),
                        holidayEntity.getTitle(),
                        holidayEntity.getHolidayDate()
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, holidayEntitiesList.getPageable(), holidayEntitiesList.getTotalElements());
    }
}
