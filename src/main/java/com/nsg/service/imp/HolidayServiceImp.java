package com.nsg.service.imp;

import com.nsg.Mapper.HolidayMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
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
    public HolidayResponse updateHoliday(String holidayId, HolidayRequest request) {
        HolidayEntity holiday = holidayRepository.findById(holidayId).orElseThrow(
                () -> new AppException(ErrorCode.HOLIDAY_NOT_FOUND)
        );
        holiday.setTitle(request.getTitle());
        holiday.setHolidayDate(request.getHolidayDate());

        holidayRepository.save(holiday);
        return getHoliday(holidayId);
    }

    @Override
    public void deleteHoliday(String holidayId) {
        holidayRepository.deleteById(holidayId);
    }

    @Override
    public HolidayEntity getHolidaytById(String holidayId) {
        return holidayRepository.findByHolidayId(holidayId).orElseThrow(
                () -> new AppException(ErrorCode.HOLIDAY_NOT_FOUND)
        );
    }

    @Override
    public HolidayResponse getHoliday(String holidayId) {
        HolidayEntity holiday = holidayRepository.findById(holidayId).orElseThrow(
                () -> new AppException(ErrorCode.HOLIDAY_NOT_FOUND)
        );

        HolidayResponse holidayResponse = new HolidayResponse();
        holidayResponse.setHolidayId(holiday.getHolidayId());
        holidayResponse.setHolidayDate(holiday.getHolidayDate());
        holidayResponse.setTitle(holiday.getTitle());
        return holidayResponse;
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
