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
    @Query("SELECT r FROM RoomEntity r WHERE NOT EXISTS (" +
            "SELECT 1 FROM SessionEntity s " +
            "WHERE s.roomEntity.roomId = r.roomId " +
            "AND s.date = :date " +
            "AND s.timeSlotEntity.timeSlotId = :timeSlotId)")
    List<RoomEntity> findAvailableRooms(@Param("date") LocalDate date,
                                        @Param("timeSlotId") String timeSlotId);

    //get available room for each time create schedule
    @Query(value = "SELECT r.* " +
            "FROM room r " +
            "LEFT JOIN session s " +
            "ON r.room_id = s.room_id " +
            "AND s.time_slot_id = :timeSlotId " +
            "WHERE s.room_id IS NULL",
            nativeQuery = true)
    List<RoomEntity> findAvailableRooms(@Param("timeSlotId") String timeSlotId);

}
