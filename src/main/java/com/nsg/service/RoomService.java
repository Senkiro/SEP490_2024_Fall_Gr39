package com.nsg.service;

import com.nsg.dto.request.room.RoomRequest;
import com.nsg.dto.response.room.RoomResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoomService {
    //create new room
    RoomResponse createRoom(RoomRequest request);

    //get all room
    List<RoomResponse> getAllRoom();

    //get available room for session
    List<RoomResponse> getAvailableRoomForSession(String sessionId);

    List<RoomResponse> getAvailableRoomForSchedule(String timeSlotId);

    //get a room by id
    RoomResponse getRoom(String roomId);

    //update room
    RoomResponse updateRoom(String roomId, RoomRequest request);

    //delete room by id
    void deleteRoom(String roomId);

}
