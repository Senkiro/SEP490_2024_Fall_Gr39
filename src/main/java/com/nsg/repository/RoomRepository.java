package com.nsg.repository;

import com.nsg.entity.RoomEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends BaseRepository<RoomEntity, String> {
}
