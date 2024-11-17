package com.nsg.service;

import com.nsg.dto.request.timeSlot.TimeSlotCreationRequest;
import com.nsg.dto.request.timeSlot.TimeSlotUpdateRequest;
import com.nsg.dto.response.timeSlot.TimeSlotResponse;
import com.nsg.entity.LessonEntity;
import com.nsg.entity.TimeSlotEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TimeSlotService {

    //create new time slot
    TimeSlotResponse createTimeSlot(TimeSlotCreationRequest request);

    //get all time slot
    List<TimeSlotResponse> getAllTimeSlot();

    //get all time slot with paging
    Page<TimeSlotEntity> getTimeSlots(int page, int size);

    //get one time slot by id
    TimeSlotResponse getTimeSlotById(String timeSlotId);

    //update one time slot
    TimeSlotResponse updateTimeSlotById(String timeSlotId, TimeSlotUpdateRequest request);

    //delete time slot
    void deleteTimeSlot(String timeSlotId);

}
