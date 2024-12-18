package com.nsg.service.imp;

import com.nsg.Mapper.*;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.session.ScheduleCreationRequest;
import com.nsg.dto.request.session.SessionCreattionRequest;
import com.nsg.dto.request.session.SessionUpdateRequest;
import com.nsg.dto.response.session.SessionResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.*;
import com.nsg.repository.*;
import com.nsg.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class SessionServiceImp implements SessionService {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    CurriculumnRepository curriculumnRepository;

    @Autowired
    CurriculumnService curriculumnService;

    @Autowired
    CurriculumnListRepository curriculumnListRepository;

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    HolidayRepository holidayRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    StudentRepository studentRepository;




    @Override
    public void createSession(SessionCreattionRequest request) {
        SessionEntity session = new SessionEntity();

        session.setDate(request.getDate());
        session.setStatus(false);
        session.setSessionAvailable(request.isSessionAvailable());

        session.setSessionNumber(request.getSessionNumber());
        session.setSessionWeek(request.getSessionWeek());

        //class
        ClassEntity classEntity = classRepository.findById(request.getClassId()).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );
        session.setClassEntity(classEntity);

        if (request.getNote() != null) {
            session.setNote(request.getNote());
        }

        //time slot
        if (request.getTimeSlotId() != null) {
            TimeSlotEntity timeSlotEntity = timeSlotRepository.findById(request.getTimeSlotId()).orElseThrow(
                    () -> new AppException(ErrorCode.TIME_SLOT_NOT_FOUND)
            );
            session.setTimeSlotEntity(timeSlotEntity);
        } else {
            session.setTimeSlotEntity(null);
        }

        //room
        if (!Objects.equals(request.getRoomNumber(), null)) {
            RoomEntity roomEntity = roomRepository.findByRoomNumber(request.getRoomNumber()).orElseThrow(
                    () -> new AppException(ErrorCode.ROOM_NOT_FOUND)
            );
            session.setRoomEntity(roomEntity);
        } else {
            session.setRoomEntity(null);
        }

        //event
        if (!Objects.equals(request.getEventId(), null)) {
            EventEntity eventEntity = eventRepository.findByEventId(request.getEventId()).orElseThrow(
                    () -> new AppException(ErrorCode.EVENT_NOT_FOUND)
            );
            session.setEventEntity(eventEntity);
        } else {
            session.setEventEntity(null);
        }

        //teacher
        if (!Objects.equals(request.getUserId(), null)) {
            UserEntity teacher = userRepository.findById(request.getUserId()).orElseThrow(
                    () -> new AppException(ErrorCode.USER_NOT_FOUND)
            );
            session.setUser(teacher);
        } else {
            session.setUser(null);
        }
        sessionRepository.save(session);
    }

    @Override
    public void createSchedule(String class_id, ScheduleCreationRequest request) {

        //test list
//        List<SessionCreattionRequest> testList = new ArrayList<>();

        //get session by className and batchName
        //check class: find class by className and batchName
        ClassEntity classEntity = classRepository.findById(class_id).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );

        //check student list in class
        if (classEntity.getStudentEntityList().isEmpty()) {
            throw new AppException(ErrorCode.STUDENT_LIST_IS_EMPTY);
        }

        //check schedule for one class existed by: find class by id
        //if existed -> throw exception
        if (sessionRepository.existsByClassEntityClassId(classEntity.getClassId())) {
            throw new AppException(ErrorCode.SCHEDULE_EXISTED);
        }

        //get batch and count days in batch
        BatchEntity batchEntity = batchRepository.findByBatchName(classEntity.getBatchEntity().getBatchName()).orElseThrow(
                () -> new AppException(ErrorCode.BATCH_NOT_EXISTED)
        );

        LocalDate startDate = batchEntity.getStartTime();

        //get curriculumn list
        CurriculumnListEntity curriculumnListEntity = curriculumnListRepository.findById(String.valueOf(request.getCurriculumnListId())).orElseThrow(
                () -> new AppException(ErrorCode.CURRICULUMN_LIST_NOT_FOUND)
        );

        //get list of curriculumn inside curriculumnList entity
        List<CurriculumnEntity> curriculumnEntityList = curriculumnListEntity.getCurriculumnEntityList();

        //check curriculumn list empty
        if (curriculumnEntityList.isEmpty()) {
            throw new AppException(ErrorCode.LIST_CURRICULUMN_EMPTY);
        }

        //get 2 time_slot entity
        List<TimeSlotEntity> timeSlotEntityList = timeSlotRepository.findAll();
        String morning = timeSlotRepository.findByName("Morning").getTimeSlotId();
        String afternoon = timeSlotRepository.findByName("Afternoon").getTimeSlotId();

        //check time_slot
        TimeSlotEntity check = timeSlotRepository.findById(request.getTimeSlotId()).orElseThrow(
                () -> new AppException(ErrorCode.TIME_SLOT_NOT_FOUND)
        );

        //get list of holiday
        List<HolidayEntity> holidays = holidayRepository.findAll();

        //get timeslot would be set to all

        int sessionNo = 1; //count session available by day
        int totalDay = 1; //count days
        int week = 0; //count week
        int count_ts = 1; //count session by time_slot

        int total_session = curriculumnEntityList.size();

        //create session by day and time_slot
        while (sessionNo <= total_session) {
            LocalDate dateOfSession = startDate.plusDays((totalDay-1));
            //create session
            SessionCreattionRequest sessionCreattionRequest = new SessionCreattionRequest();
            sessionCreattionRequest.setDate(dateOfSession);
            sessionCreattionRequest.setClassId(classEntity.getClassId());

            DayOfWeek dow = dateOfSession.getDayOfWeek();

            sessionCreattionRequest.setSessionNumber(count_ts);

            //if %count_ts = 1 -> morning
            if (count_ts % 2 == 1) {
                sessionCreattionRequest.setTimeSlotId(morning);
            }
            //else if % count_ts = 0 -> afternoon
            else if (count_ts % 2 == 0) {
                sessionCreattionRequest.setTimeSlotId(afternoon);
            }

            sessionCreattionRequest.setSessionWeek(week);


            if (dow == DayOfWeek.SATURDAY
                    || dow == DayOfWeek.SUNDAY) {
                //create blank session
                sessionCreattionRequest.setSessionAvailable(false);
                //week break
                if (dow == DayOfWeek.SUNDAY && count_ts % 2 == 0){
                    week += 1;
                }

            } else if (checkHoliday(holidays, dateOfSession) != null) {
                //create blank session
                sessionCreattionRequest.setSessionAvailable(false);
                sessionCreattionRequest.setNote(checkHoliday(holidays, dateOfSession));

            } else {

                //if time_slot_id = requestId -> set available is true
                if (request.getTimeSlotId().equals(sessionCreattionRequest.getTimeSlotId())){
                    sessionCreattionRequest.setSessionAvailable(true);
                    sessionCreattionRequest.setRoomNumber(request.getRoomNumber());
                } else {
                    sessionCreattionRequest.setSessionAvailable(false);
                }

                //session tang 1
                if (count_ts % 2 == 0) {
                    sessionNo++;
                }
            }
            //if %count_ts = 0 then total day increase 1
            if (count_ts % 2 == 0) {
                totalDay++;
            }

            //save session
            createSession(sessionCreattionRequest);
//            testList.add(sessionCreattionRequest);
            count_ts++;
        }
        System.out.println("Total days: "+totalDay);

//        System.out.println("TEST LIST: "+testList);

        //update total week and end date for batch
        batchEntity.setTotalWeek(week);
        LocalDate endDate = startDate.plusDays( (totalDay-1) );
        batchEntity.setEndTime(endDate);

        batchRepository.save(batchEntity);

        //fill session
        fillSession(class_id, curriculumnEntityList);
    }

    //validate room

    //fill session: only fill available session
    public void fillSession(String classId, List<CurriculumnEntity> curriculumnEntityList) {
        //get all session available by classId, then sort it by day and sessionNumber
        List<SessionEntity> availableSessions = sessionRepository.findSessionsByClassIdAndAvailableAndStatus(classId);

        //if available sesssion list null
        if (availableSessions.isEmpty()) {
            throw new AppException(ErrorCode.NO_SESSION_AVAILABLE);
        }

        int count_curriculumn = 0;

        for (SessionEntity session : availableSessions) {
            session.setCurriculumnEntity(curriculumnEntityList.get(count_curriculumn));

            //set default status
            session.setAttendanceStatus("Not happen");
            session.setMarkStatus("Not happen");

            count_curriculumn += 1;
        }

        //save all
        sessionRepository.saveAll(availableSessions);

    }
    //add event -> session change to available

    //check one day is holiday or not?
    public String checkHoliday(List<HolidayEntity> holidays, LocalDate dateOfSession) {
        for (HolidayEntity holiday: holidays) {
            if (holiday.getHolidayDate().equals(dateOfSession)) {
                return holiday.getTitle();
            }
        }
        return null;
    }

    //get session by session id and time_slot_id
    //autofill teacher on session when update
    @Override
    public void autoFillTeacherToSession(String teacherId,
                                         String classId,
                                         String sessionId,
                                         int weekEnd) {
        //get teacher
        UserEntity teacher = userRepository.findById(teacherId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        //get session
        SessionEntity session = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        //get date of first session
        LocalDate startDate = session.getDate();

        //get all session need to set teacher
        List<SessionEntity> sessionEntityList = sessionRepository.findSessionsByDateTimeSlotClass(classId,
                    String.valueOf(startDate),
                    weekEnd,
                    session.getTimeSlotEntity().getTimeSlotId());

        for (SessionEntity sessionEntity : sessionEntityList) {
            //set teacher for each session if session is available
            if ( sessionEntity.isSessionAvailable() ) {
                sessionEntity.setUser(teacher);
            }
        }

        //save all
        sessionRepository.saveAll(sessionEntityList);
    }

    @Override
    public List<UserInforResponse> getAvailableTeachers(String sessionId) {

        //get date and timeSlotId
        SessionEntity session = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        List<UserEntity> listTeacher = teacherRepository.findAvailableTeachers(session.getDate(),
                session.getTimeSlotEntity().getTimeSlotId());

        List<UserInforResponse> responseList = new ArrayList<>();

        for (UserEntity user: listTeacher) {
            UserInforResponse temp = UserMapper.INSTANCE.toUserInforResponse(user);
            responseList.add(temp);
        }

        return responseList;
    }

    @Override
    public List<SessionResponse> getSessionUnavailable(String classId, int sessionWeek) {
        List<SessionEntity> sessionEntities = sessionRepository.findSessionsUnavailableByClassId(classId, sessionWeek);

        return toListSessionResponse(sessionEntities);
    }


    @Override
    public void deleteSchedule(String classId) {
        //delete all session have class_id equal to class id receive
        //find session by class id
        List<SessionEntity> sessionEntities = sessionRepository.findByClassEntityClassId(classId);

        //for: delete each session in list
        for (SessionEntity session : sessionEntities) {

            //get session id
            String sessionId = session.getSessionId();

            deleteSession(sessionId);

        }
    }

    @Override
    public Page<SessionResponse> getAllSession(int page, int size) {
        //get all
        Page<SessionEntity> sessionEntities = sessionRepository.findAll(PageRequest.of(page, size));

        List<SessionResponse> responseList = toListSessionResponse(sessionEntities.getContent());
        return new PageImpl<>(responseList, sessionEntities.getPageable(), sessionEntities.getTotalElements());
    }

    @Override
    public List<SessionResponse> getSessionByClassAndWeek(int week, String classId) {
        //get list of session by week and classId
        List<SessionEntity> sessionEntities = sessionRepository.findBySessionWeekAndClassEntityClassId(week, classId);

        return toListSessionResponse(sessionEntities);
    }

    @Override
    public List<SessionResponse> getSessionByClassAndTeacher(String teacherId) {

        if (userRepository.findById(teacherId).isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        //get list of session by teacher
        List<SessionEntity> sessionEntities = sessionRepository.findByUserId(teacherId);
        if (!sessionEntities.isEmpty()) {
            return toListSessionResponse(sessionEntities);
        } else {
            return null;
        }
    }

    @Override
    public List<SessionResponse> getSessionByAttendanceStatus(String classId) {
        //get list of session by class
        List<SessionEntity> sessionEntities = sessionRepository.findSessionsAttendanceStatus(classId);
        if (!sessionEntities.isEmpty()) {
            return toListSessionResponse(sessionEntities);
        } else {
            return null;
        }
    }

    public List<SessionResponse> toListSessionResponse(List<SessionEntity> sessionEntities) {

        List<SessionResponse> responseList = new ArrayList<>();

        for (SessionEntity session : sessionEntities) {
            SessionResponse tempResponse = new SessionResponse();
            tempResponse.setSessionId(session.getSessionId());
            tempResponse.setNote(session.getNote());
            tempResponse.setSessionNumber(session.getSessionNumber());
            tempResponse.setSessionWeek(session.getSessionWeek());

            LocalDate localDate = session.getDate();
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();

            tempResponse.setDayOfWeek(dayOfWeek);

            tempResponse.setDate(session.getDate());
            tempResponse.setStatus(session.isStatus());
            tempResponse.setSessionAvailable(session.isSessionAvailable());

            tempResponse.setAttendanceStatus( session.getAttendanceStatus() );
            tempResponse.setMarkStatus(session.getMarkStatus());

            tempResponse.setClassResponse(ClassMapper.INSTANCE.toClassResponse(session.getClassEntity()));

            //check null
            if (session.getCurriculumnEntity() != null) {
                tempResponse.setCurriculumnResponse(curriculumnService.toCurriculumnResponse( session.getCurriculumnEntity() ));
            }

            tempResponse.setTimeSlotResponse(timeSlotService.toTimeSlotResponse( session.getTimeSlotEntity() ));

            if (session.getRoomEntity() != null) {
                tempResponse.setRoomNumber(session.getRoomEntity().getRoomNumber());
            } else {
                tempResponse.setRoomNumber(null);
            }

            if (session.getEventEntity() != null) {
                tempResponse.setEventName(session.getEventEntity().getEventName());
                tempResponse.setEventId(session.getEventEntity().getEventId());

                tempResponse.setEventResponse( EventMapper.INSTANCE.toEventResponse( session.getEventEntity() ));

            }else {
                tempResponse.setEventName(null);
            }

            if (session.getUser() != null) {
                tempResponse.setFullName(session.getUser().getFullName());
                tempResponse.setEmail(session.getUser().getEmail());
                tempResponse.setUserId(session.getUser().getUserId());
            } else {
                tempResponse.setFullName(null);
                tempResponse.setEmail(null);
            }

            responseList.add(tempResponse);
        }
        //sort
        Collections.sort(responseList, Comparator.comparingInt(SessionResponse::getSessionNumber));
        return responseList;
    }

    @Override
    public SessionResponse getSession(String sessionId) {
        //get session
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );
        return toSessionResponse(sessionEntity);
    }

    //get session by studentId
    @Override
    public List<SessionResponse> getSessionByStudentId(String studentId) {
        //get student entity
        StudentEntity student = studentRepository.findById(studentId).orElseThrow(
                () -> new AppException(ErrorCode.STUDENT_NOT_FOUND)
        );
        //get class entity in student entity
        ClassEntity classEntity = student.getClassEntity();

        //find session by classId
        List<SessionEntity> sessionEntityList = sessionRepository.findByClassEntityClassId(classEntity.getClassId());

        return toListSessionResponseForStudent(sessionEntityList, studentId);
    }

    public List<SessionResponse> toListSessionResponseForStudent(List<SessionEntity> sessionEntities, String studentId) {

        List<SessionResponse> responseList = new ArrayList<>();

        for (SessionEntity session : sessionEntities) {
            SessionResponse tempResponse = new SessionResponse();
            tempResponse.setSessionId(session.getSessionId());
            tempResponse.setNote(session.getNote());
            tempResponse.setSessionNumber(session.getSessionNumber());
            tempResponse.setSessionWeek(session.getSessionWeek());

            LocalDate localDate = session.getDate();
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();

            tempResponse.setDayOfWeek(dayOfWeek);

            tempResponse.setDate(session.getDate());
            tempResponse.setStatus(session.isStatus());
            tempResponse.setSessionAvailable(session.isSessionAvailable());

            //this would be attendance status of student in that session

            //get attendance by session id
            List<AttendanceEntity> attendanceEntityList = attendanceRepository.findBySessionEntitySessionId(session.getSessionId());
            for (AttendanceEntity attendanceEntity : attendanceEntityList) {
                if (attendanceEntity.getStudentEntity().getStudentId().equals( studentId )) {
                    tempResponse.setAttendanceStatus( attendanceEntity.getStatus() );
                }
            }

            tempResponse.setMarkStatus(session.getMarkStatus());

            tempResponse.setClassResponse(ClassMapper.INSTANCE.toClassResponse(session.getClassEntity()));

            //check null
            if (session.getCurriculumnEntity() != null) {
                tempResponse.setCurriculumnResponse(curriculumnService.toCurriculumnResponse( session.getCurriculumnEntity() ));
            }

            tempResponse.setTimeSlotResponse(timeSlotService.toTimeSlotResponse( session.getTimeSlotEntity() ));

            if (session.getRoomEntity() != null) {
                tempResponse.setRoomNumber(session.getRoomEntity().getRoomNumber());
            } else {
                tempResponse.setRoomNumber(null);
            }

            if (session.getEventEntity() != null) {
                tempResponse.setEventName(session.getEventEntity().getEventName());
                tempResponse.setEventId(session.getEventEntity().getEventId());

                tempResponse.setEventResponse( EventMapper.INSTANCE.toEventResponse( session.getEventEntity() ));

            }else {
                tempResponse.setEventName(null);
            }

            if (session.getUser() != null) {
                tempResponse.setFullName(session.getUser().getFullName());
                tempResponse.setEmail(session.getUser().getEmail());
                tempResponse.setUserId(session.getUser().getUserId());
            } else {
                tempResponse.setFullName(null);
                tempResponse.setEmail(null);
            }

            responseList.add(tempResponse);
        }
        //sort
        Collections.sort(responseList, Comparator.comparingInt(SessionResponse::getSessionNumber));
        return responseList;
    }

    //get session by exam of a class
    @Override
    public List<SessionResponse> getSessionByExamNotNull(String classId) {
        //get class entity in student entity
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );

        //find session by classId and exam
        List<SessionEntity> sessionEntityList = sessionRepository.findSessionsByClassIdAndExamExists(classId);

        return toListSessionResponse(sessionEntityList);
    }

    //get session have exam by class and teacher
    @Override
    public List<SessionResponse> getSessionByExamNotNullAndTeacherId(String classId, String teacherId) {
        //get class entity in student entity
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );

        //find session by classId and exam
        List<SessionEntity> sessionEntityList = sessionRepository.findSessionsByClassIdAndTeacherIdAndExamExists(classId, teacherId);

        return toListSessionResponse(sessionEntityList);
    }

    @Override
    public SessionResponse toSessionResponse(SessionEntity sessionEntity) {
        //mapping
        SessionResponse response = new SessionResponse();
        response.setSessionId(sessionEntity.getSessionId());
        response.setSessionNumber(sessionEntity.getSessionNumber());
        response.setSessionWeek(sessionEntity.getSessionWeek());

        LocalDate localDate = sessionEntity.getDate();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        response.setDayOfWeek(dayOfWeek);

        response.setMarkStatus(sessionEntity.getMarkStatus());
        response.setAttendanceStatus(sessionEntity.getAttendanceStatus());

        response.setDate(sessionEntity.getDate());
        response.setStatus(sessionEntity.isStatus());

        response.setClassResponse(ClassMapper.INSTANCE.toClassResponse(sessionEntity.getClassEntity()));

        if (sessionEntity.getMarkStatus() != null) {
            response.setMarkStatus(sessionEntity.getMarkStatus());
        }

        if (sessionEntity.getAttendanceStatus() != null) {
            response.setAttendanceStatus(sessionEntity.getAttendanceStatus());
        }

        if (sessionEntity.getCurriculumnEntity() != null) {
            response.setCurriculumnResponse( curriculumnService.toCurriculumnResponse( sessionEntity.getCurriculumnEntity() ) );
        }

        response.setTimeSlotResponse( timeSlotService.toTimeSlotResponse( sessionEntity.getTimeSlotEntity() ));

        if (sessionEntity.getRoomEntity() != null) {
            response.setRoomNumber(sessionEntity.getRoomEntity().getRoomNumber());
        } else {
            response.setRoomNumber(null);
        }

        if (sessionEntity.getEventEntity() != null) {
            response.setEventName(sessionEntity.getEventEntity().getEventName());
        }else {
            response.setEventName(null);
        }

        if (sessionEntity.getUser() != null) {
            response.setFullName(sessionEntity.getUser().getFullName());
            response.setEmail(sessionEntity.getUser().getEmail());
        } else {
            response.setFullName(null);
            response.setEmail(null);
        }

        return response;
    }

    @Override
    public void updateOnlySessionStatus(String sessionId) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );
        sessionEntity.setStatus(!sessionEntity.isStatus());
        sessionRepository.save(sessionEntity);
    }

    @Override
    public void updateSessionAttendanceStatus(String sessionId, String newStatus) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );
        sessionEntity.setAttendanceStatus(newStatus);
        sessionRepository.save(sessionEntity);
    }

    @Override
    public void updateSessionMarkStatus(String sessionId, String newStatus) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );
        sessionEntity.setMarkStatus(newStatus);
        sessionRepository.save(sessionEntity);
    }

    @Override
    public SessionResponse updateSession(String sessionId, SessionUpdateRequest request) {
        SessionEntity sessionEntity = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        //convert
        sessionEntity.setSessionAvailable( request.isSessionAvailable() );

        if (request.getAttendanceStatus() != null) {
            sessionEntity.setAttendanceStatus(request.getAttendanceStatus());
        }

        if (request.getMarkStatus() != null) {
            sessionEntity.setMarkStatus(request.getMarkStatus() );
        }

        if (request.getNote() != null) {
            sessionEntity.setNote(request.getNote() );
        }

        //curriculumn
        if (request.getCurriculumnId() != null) {
            CurriculumnEntity curriculumn = curriculumnRepository.findById(String.valueOf(request.getCurriculumnId())).orElseThrow(
                    () -> new AppException(ErrorCode.CURRICULUMN_NOT_FOUND)
            );
            sessionEntity.setCurriculumnEntity(curriculumn);
        } else {
            sessionEntity.setCurriculumnEntity(null);
        }

        //room
        if (request.getRoomNumber() == null || request.getRoomNumber().isEmpty()) {
            sessionEntity.setRoomEntity(null);
        } else {
            RoomEntity room = roomRepository.findByRoomNumber(request.getRoomNumber()).orElseThrow(
                    () -> new AppException(ErrorCode.ROOM_NOT_FOUND)
            );
            sessionEntity.setRoomEntity(room);
        }

        //event
        if (request.getEventId() == null || request.getEventId().isEmpty()) {
            sessionEntity.setEventEntity(null);
        } else {
            sessionEntity.setEventEntity(eventRepository.findByEventId(request.getEventId()).orElseThrow(
                    () -> new AppException(ErrorCode.EVENT_NOT_FOUND))
            );
        }

        //teacher
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            sessionEntity.setUser(null);
        } else {
            sessionEntity.setUser(userRepository.findById(request.getUserId()).orElseThrow(
                    () -> new AppException(ErrorCode.USER_NOT_FOUND)
            ));
        }

        //save
        sessionRepository.save(sessionEntity);

        return getSession(sessionId);
    }

    @Transactional
    @Override
    public void deleteSession(String sessionId) {
        SessionEntity session = sessionRepository.findById(sessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        // Xóa dữ liệu liên quan (ví dụ: attendanceEntityList)
        attendanceRepository.deleteBySessionEntity(session);

        // Sau đó xóa session
        try {
            sessionRepository.deleteSessionBySessionId(session.getSessionId());
        } catch (Exception e) {
            System.out.println("ERRRRRRROR: "+e.getMessage());
            throw e;
        }
    }

    @Override
    public void swapToUnavailableSession(String currentSessionId, String toSessionId) {
        //get current session
        SessionEntity currentSession = sessionRepository.findById(currentSessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        //get to session
        SessionEntity toSession = sessionRepository.findById(toSessionId).orElseThrow(
                () -> new AppException(ErrorCode.SESSION_NOT_FOUND)
        );

        //only change date, time_slot_id, session number, week, status of two session

        //get date, time_slot_id, session_number, week of toSession and current session
        LocalDate toDate = toSession.getDate();
        TimeSlotEntity toTimeSlot = toSession.getTimeSlotEntity();
        int toSessionNumber = toSession.getSessionNumber();
        int toWeek = toSession.getSessionWeek();
        boolean toStatus = toSession.isStatus();

        LocalDate currentDate = currentSession.getDate();
        TimeSlotEntity currentTimeSlot = currentSession.getTimeSlotEntity();
        int currentSessionNumber = currentSession.getSessionNumber();
        int currentWeek = currentSession.getSessionWeek();
        boolean currentStatus = currentSession.isStatus();

        //change date of current session
        currentSession.setDate( toDate );
        currentSession.setTimeSlotEntity( toTimeSlot );
        currentSession.setSessionNumber( toSessionNumber );
        currentSession.setSessionWeek( toWeek );
        currentSession.setStatus( toStatus );

        //change date and time slot of to session
        toSession.setDate( currentDate );
        toSession.setTimeSlotEntity( currentTimeSlot );
        toSession.setSessionNumber( currentSessionNumber );
        toSession.setSessionWeek( currentWeek );
        toSession.setStatus( currentStatus );

        //save
        sessionRepository.save( currentSession );
        sessionRepository.save( toSession );

    }

    //get all session in batch on-progress
    @Override
    public List<SessionResponse> getSessionInBatch() {

        //get list of session by teacher
        List<SessionEntity> sessionEntities = sessionRepository.findAvailableSessions();
        if (!sessionEntities.isEmpty()) {
            return toListSessionResponse(sessionEntities);
        } else {
            return null;
        }
    }

}
