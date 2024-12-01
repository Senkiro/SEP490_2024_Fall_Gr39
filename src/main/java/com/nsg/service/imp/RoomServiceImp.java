package com.nsg.service.imp;

import com.nsg.Mapper.RoomMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.room.RoomRequest;
import com.nsg.dto.response.room.RoomResponse;
import com.nsg.entity.RoomEntity;
import com.nsg.entity.SessionEntity;
import com.nsg.repository.RoomRepository;
import com.nsg.repository.SessionRepository;
import com.nsg.service.RoomService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImp implements RoomService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Override
    public RoomResponse createRoom(RoomRequest request) {
        RoomEntity room = new RoomEntity();
        //map
        BeanUtils.copyProperties(request, room);

        return RoomMapper.INSTANE.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public List<RoomResponse> getAllRoom() {
        List<RoomEntity> roomEntityList = roomRepository.findAll();
        List<RoomResponse> responseList = new ArrayList<>();

        for (RoomEntity room: roomEntityList) {
            RoomResponse tempRoom = new RoomResponse();
            tempRoom.setRoomId(room.getRoomId());
            tempRoom.setRoomNumber(room.getRoomNumber());

            responseList.add(tempRoom);
        }
        return responseList;
    }

    @Override
    public List<RoomResponse> getAvailableRoomForSession(String sessionId) {

        SessionEntity session = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        List<RoomEntity> roomEntityList = roomRepository.findAvailableRooms(session.getDate(), session.getTimeSlotEntity().getTimeSlotId());
        List<RoomResponse> responseList = new ArrayList<>();

        for (RoomEntity room: roomEntityList) {
            RoomResponse tempRoom = new RoomResponse();
            tempRoom.setRoomId(room.getRoomId());
            tempRoom.setRoomNumber(room.getRoomNumber());

            responseList.add(tempRoom);
        }
        return responseList;
    }

    @Override
    public RoomResponse getRoom(String roomId) {
        RoomEntity room = roomRepository.findById(roomId).orElseThrow(
                () -> new AppException(ErrorCode.ROOM_NOT_FOUND)
        );
        return RoomMapper.INSTANE.toRoomResponse(room);
    }

    @Override
    public RoomResponse updateRoom(String roomId, RoomRequest request) {
        //get room by id
        RoomEntity room = roomRepository.findById(roomId).orElseThrow(
                () -> new AppException(ErrorCode.ROOM_NOT_FOUND)
        );

        RoomMapper.INSTANE.updateRoomEntityFromRequest(request, room);

        return RoomMapper.INSTANE.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public void deleteRoom(String roomId) {
        roomRepository.deleteById(roomId);
    }
}
