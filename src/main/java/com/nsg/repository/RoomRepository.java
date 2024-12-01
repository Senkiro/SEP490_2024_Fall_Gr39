package com.nsg.repository;

import com.nsg.entity.RoomEntity;
import com.nsg.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends BaseRepository<RoomEntity, String> {
    Optional<RoomEntity> findByRoomNumber(String roomNumber);

    //get available room in each session
    @Query("SELECT r FROM RoomEntity r WHERE" +
            "NOT EXISTS (" +
            "   SELECT 1 FROM SessionEntity s WHERE s.roomEntity.roomId = r.roomId " +
            "   AND s.date = :date " +
            "   AND s.timeSlotEntity.timeSlotId = :timeSlotId" +
            ")")
    List<RoomEntity> findAvailableRooms(@Param("date") LocalDate date,
                                           @Param("timeSlotId") String timeSlotId);
}
