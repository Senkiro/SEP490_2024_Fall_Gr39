package com.nsg.repository;

import com.nsg.entity.SessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface SessionRepository extends BaseRepository<SessionEntity, String> {
    List<SessionEntity> findBySessionWeekAndClassEntityClassId(int week, String classId);

    @Query("SELECT s FROM SessionEntity s WHERE s.user.id = :userId")
    List<SessionEntity> findByUserId(@Param("userId") String userId);

    boolean existsByClassEntityClassId(String classId);

    List<SessionEntity> findByClassEntityClassId(String classId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Session WHERE session_id = :sessionId", nativeQuery = true)
    void deleteSessionBySessionId(@Param("sessionId") String sessionId);

    //get session by: classId, session_available = true, status = false; sort by date and sessionNumber
    @Query(value = "SELECT * FROM Session " +
            "WHERE class_id = :classId " +
            "AND session_available = TRUE " +
            "AND status = FALSE " +
            "ORDER BY date ASC, session_number ASC",
            nativeQuery = true)
    List<SessionEntity> findSessionsByClassIdAndAvailableAndStatus(
            @Param("classId") String classId);

    @Query(value = "SELECT * FROM Session " +
            "WHERE class_id = :classId " +
            "AND session_available = FALSE " +
            "AND status = FALSE " +
            "AND session_week = :sessionWeek " +
            "ORDER BY date ASC, session_number ASC",
            nativeQuery = true)
    List<SessionEntity> findSessionsUnavailableByClassId(
            @Param("classId") String classId,
            @Param("sessionWeek") int sessionWeek );

    @Query(value = "SELECT DISTINCT s.* " +
            "FROM session s " +
            "JOIN curriculumn c ON s.curriculumn_id = c.curriculumn_id " +
            "WHERE s.class_id = :classId " +
            "AND c.exam_id IS NOT NULL",
            nativeQuery = true)
    List<SessionEntity> findSessionsByClassIdAndExamExists(@Param("classId") String classId);

    @Query(value = "SELECT DISTINCT s.* " +
            "FROM session s " +
            "JOIN curriculumn c ON s.curriculumn_id = c.curriculumn_id " +
            "WHERE s.class_id = :classId " +
            "AND s.user_id = :teacherId " +
            "AND c.exam_id IS NOT NULL",
            nativeQuery = true)
    List<SessionEntity> findSessionsByClassIdAndTeacherIdAndExamExists(@Param("classId") String classId,
                                                                       @Param("teacherId") String teacherId);

    @Query(value = "SELECT * FROM Session " +
            "WHERE class_id = :classId " +
            "AND attendance_status is not null " +
            "ORDER BY date ASC, session_number ASC",
            nativeQuery = true)
    List<SessionEntity> findSessionsAttendanceStatus(@Param("classId") String classId);

    @Query(value = "SELECT * FROM Session " +
            "WHERE class_id = :classId " +
            "AND attendance_status is not null " +
            "AND s.user_id = :teacherId " +
            "ORDER BY date ASC, session_number ASC",
            nativeQuery = true)
    List<SessionEntity> findSessionsAttendanceStatusAndTeacher(@Param("classId") String classId,
                                                               @Param("teacherId") String teacherId);

    //find by date, time_slot_id, class_id
    @Query(value = "SELECT * FROM Session " +
            "WHERE class_id = :classId " +
            "AND date = :date " +
            "AND time_slot_id = :time_slot_id ",
            nativeQuery = true)
    SessionEntity findSessionsByDateTimeSlotClass(@Param("classId") String classId,
                                                        @Param("date") Date date,
                                                        @Param("time_slot_id") String time_slot_id);

}
