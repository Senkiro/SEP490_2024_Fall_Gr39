package com.nsg.repository;

import com.nsg.entity.RoomEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends BaseRepository<RoomEntity, String> {
    Optional<RoomEntity> findByRoomNumber(String roomNumber);
}
