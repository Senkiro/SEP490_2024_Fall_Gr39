package com.nsg.service;

import com.nsg.dto.request.holiday.HolidayRequest;
import com.nsg.dto.request.news.NewsRequest;
import com.nsg.dto.response.holiday.HolidayResponse;
import com.nsg.dto.response.news.NewsResponse;
import com.nsg.entity.HolidayEntity;
import com.nsg.entity.NewEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface HolidayService {
    HolidayResponse getHoliday(String holidayId);

    Page<HolidayResponse> getAllHoliday(int page, int size);

    HolidayResponse createHoliday(HolidayRequest request);

    HolidayResponse updateHoliday(String holidayId, HolidayRequest request);

    void deleteHoliday(String holidayId);

    HolidayEntity getHolidaytById(String holidayId);

}
