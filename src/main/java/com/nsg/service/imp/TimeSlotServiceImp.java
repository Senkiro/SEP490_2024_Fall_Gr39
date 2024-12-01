package com.nsg.service.imp;

import com.nsg.Mapper.TimeSlotMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.timeSlot.TimeSlotCreationRequest;
import com.nsg.dto.request.timeSlot.TimeSlotUpdateRequest;
import com.nsg.dto.response.timeSlot.TimeSlotResponse;
import com.nsg.entity.TimeSlotEntity;
import com.nsg.repository.TimeSlotRepository;
import com.nsg.service.TimeSlotService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeSlotServiceImp implements TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Override
    public TimeSlotResponse createTimeSlot(TimeSlotCreationRequest request) {
        TimeSlotEntity timeSlot = timeSlotRepository.findByName(request.getName());

        if (timeSlot != null) {
            throw new AppException(ErrorCode.TIME_SLOT_NAME_EXISTED);
        } else {
            timeSlot = new TimeSlotEntity();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            timeSlot.setName(request.getName());

            // Chuyển đổi String thành LocalTime
            timeSlot.setStartTime(LocalTime.parse(request.getStartTime(), formatter));
            timeSlot.setEndTime(LocalTime.parse(request.getEndTime(), formatter));

            return TimeSlotMapper.INSTANCE.toTimeSlotResponse(timeSlotRepository.save(timeSlot));
        }

    }

    @Override
    public List<TimeSlotResponse> getAllTimeSlot() {
        List<TimeSlotResponse> timeSlotResponseList = new ArrayList<>();
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findAll();
        for (TimeSlotEntity timeSlot:
             timeSlotEntityList) {
            TimeSlotResponse timeSlotResponse = new TimeSlotResponse();

            timeSlotResponse.setTimeSLotId(timeSlot.getTimeSlotId());
            timeSlotResponse.setName(timeSlot.getName());
            timeSlotResponse.setStartTime(timeSlot.getStartTime());
            timeSlotResponse.setEndTime(timeSlot.getEndTime());

            timeSlotResponseList.add(timeSlotResponse);
        }
        return timeSlotResponseList;
    }

    @Override
    public Page<TimeSlotEntity> getTimeSlots(int page, int size) {

        return null;
    }

    @Override
    public TimeSlotResponse getTimeSlotById(String timeSlotId) {
        TimeSlotEntity timeSlot = timeSlotRepository.findById(timeSlotId).orElseThrow(
                () -> new AppException(ErrorCode.TIME_SLOT_NOT_FOUND)
        );
        return TimeSlotMapper.INSTANCE.toTimeSlotResponse(timeSlot);
    }

    @Override
    public TimeSlotResponse updateTimeSlotById(String timeSlotId, TimeSlotUpdateRequest request) {
        //get time slot by id
        TimeSlotEntity timeSlot = timeSlotRepository.findById(timeSlotId).orElseThrow(
                () -> new AppException(ErrorCode.TIME_SLOT_NOT_FOUND)
        );

        //map
        TimeSlotMapper.INSTANCE.updateTimeSlotFromRequest(request, timeSlot);

        return TimeSlotMapper.INSTANCE.toTimeSlotResponse(timeSlotRepository.save(timeSlot));
    }

    @Override
    public void deleteTimeSlot(String timeSlotId) {
        timeSlotRepository.deleteById(timeSlotId);
    }
}
