package com.nsg.service.imp;

import com.nsg.Mapper.ExamMapper;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.mark.MarkCreationRequest;
import com.nsg.dto.request.mark.MarkUpdateRequest;
import com.nsg.dto.response.attendance.AttendanceStatisticsResponse;
import com.nsg.dto.response.mark.MarkResponse;
import com.nsg.entity.*;
import com.nsg.repository.*;
import com.nsg.service.AttendanceService;
import com.nsg.service.ExamService;
import com.nsg.service.MarkService;
import com.nsg.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarkServiceImp implements MarkService {

    @Autowired
    MarkRepository markRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    ExamRepository examRepository;

    @Autowired
    CurriculumnRepository curriculumnRepository;

    @Autowired
    ExamService examService;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    AttendanceService attendanceService;

    @Override
    public void createMark(MarkCreationRequest request) {
        MarkEntity markEntity = new MarkEntity();

        //convert
        markEntity.setMark(request.getMark());
        markEntity.setComment(request.getComment());

        //get student and exam
        StudentEntity student = studentRepository.findById(request.getStudentId()).orElseThrow(
                () -> new AppException(ErrorCode.STUDENT_NOT_FOUND)
        );
        markEntity.setStudentEntity(student);

        ExamEntity exam = examRepository.findById(String.valueOf( request.getExamId() )).orElseThrow(
                () -> new AppException(ErrorCode.EXAM_NOT_FOUND)
        );
        markEntity.setExamEntity(exam);

        markRepository.save(markEntity);
    }

    @Override
    public Page<MarkResponse> getAllMark(int page, int size) {
        Page<MarkEntity> markEntities = markRepository.findAll(PageRequest.of(page, size));

        List<MarkResponse> responseList = toMarkResponseList(markEntities.getContent());

        return new PageImpl<>(responseList, markEntities.getPageable(), markEntities.getTotalElements());
    }

    public MarkResponse toMarkResponse(MarkEntity markEntity) {
        MarkResponse markResponse = new MarkResponse();

        markResponse.setMarkId(markEntity.getMarkId());
        markResponse.setMark(markEntity.getMark());
        markResponse.setComment(markEntity.getComment());

        markResponse.setStudentResponse(studentService.convertToStudentResponse(markEntity.getStudentEntity()));

        markResponse.setExamResponse(ExamMapper.INSTANCE.toExamResponse(markEntity.getExamEntity()));

        return markResponse;
    }

    public List<MarkResponse> toMarkResponseList(List<MarkEntity> markEntityList) {
        List<MarkResponse> responseList = new ArrayList<>();

        for (MarkEntity mark : markEntityList) {
            MarkResponse temp = toMarkResponse(mark);
            responseList.add(temp);
        }

        return responseList;
    }

    @Override
    public MarkResponse getMark(String markId) {
        MarkEntity markEntity = markRepository.findById(markId).orElseThrow(
                () -> new AppException(ErrorCode.MARK_NOT_FOUND)
        );

        return toMarkResponse(markEntity);
    }

    @Override
    public List<MarkResponse> getMarkByStudent(String studentId) {

        //check studentId
        if (!studentRepository.existsById(studentId)) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }

        List<MarkEntity> markEntityList = markRepository.findByStudentEntityStudentId(studentId);
        if (!markEntityList.isEmpty()) {
            return toMarkResponseList(markEntityList);
        } else {
            throw new AppException(ErrorCode.MARK_LIST_EMPTY);
        }
    }

    @Override
    public void calculateAverageMark(String studentId) {
        // Lấy danh sách MarkEntity
        List<MarkEntity> markEntityList = markRepository.findByStudentEntityStudentId(studentId);

        // Kiểm tra danh sách rỗng để tránh lỗi chia cho 0
        if (markEntityList.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy điểm của sinh viên với ID: " + studentId);
        }

        // Tính tổng điểm
        double totalMark = 0.0;
        for (MarkEntity mark : markEntityList) {
            int rate = Integer.parseInt(mark.getExamEntity().getExamTypeRateEntity().getExamRate());
            totalMark += (mark.getMark() * rate / 100.0);
        }

        // Tính trung bình
        totalMark /= markEntityList.size();

        // Lấy thống kê điểm danh
        AttendanceStatisticsResponse attendanceStatisticsResponse = attendanceService.getDataAttendanceStatisticsResponse(studentId);

        // Cộng thêm điểm thưởng từ tỷ lệ điểm danh
        totalMark += attendanceStatisticsResponse.getAttendPercentage() * 0.1;

        float avgMark = (float) totalMark;
        System.out.println("Avg mark: "+avgMark);

        //update student avg mark
        studentService.markUpdate(studentId, avgMark);
    }

    @Override
    public MarkResponse updateMark(String markId, MarkUpdateRequest request) {
        MarkEntity markEntity = markRepository.findById(markId).orElseThrow(
                () -> new AppException(ErrorCode.MARK_NOT_FOUND)
        );

        //convert
        markEntity.setMark(request.getMark());
        markEntity.setStatus(request.isStatus());
        markEntity.setComment(request.getComment());

        if (request.getUpdatedBy() != null) {
            markEntity.setUpdatedBy(request.getUpdatedBy());
        }

        markRepository.save(markEntity);

        return getMark(markId);
    }

    @Override
    public void deleteMark(String markId) {
        markRepository.deleteById(markId);
    }

    //generate mark for each student in class equal to number of exam in curriculum
    public void generateMarkForAllStudentInClass(String classId) {
        //get class
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );

        //get all exam of student in class
        List<ExamEntity> examEntityList = examRepository.findExamsByClassId(classId);

        //get student list
        List<StudentEntity> studentEntityList = classEntity.getStudentEntityList();

        //check null
        if (studentEntityList.isEmpty()) {
            throw new AppException(ErrorCode.STUDENT_LIST_IS_EMPTY);
        }

        //generate mark for each student
        for (StudentEntity student : studentEntityList) {
            generateMarkEntityForOneStudent(student, examEntityList);
        }
    }

    //generate mark for one student
    public void generateMarkEntityForOneStudent(StudentEntity student, List<ExamEntity> examEntityList) {
        for (ExamEntity exam : examEntityList) {
            //create new mark for each exam
            addNewDefaultMarkEntity(student, exam);
        }
    }

    public void addNewDefaultMarkEntity(StudentEntity student, ExamEntity exam) {
        //check exist
        if (markRepository.existsByStudentEntityStudentIdAndExamEntityExamId(student.getStudentId(), exam.getExamId() )) {
            throw new AppException(ErrorCode.MARK_EXISTED);
        }

        MarkEntity markEntity = new MarkEntity();

        //set default mark and status
        float defaultMark = 0.0F;
        markEntity.setMark(defaultMark);
        markEntity.setStatus(false);

        //set student and exam
        markEntity.setStudentEntity(student);
        markEntity.setExamEntity(exam);

        markRepository.save(markEntity);
    }

    //generate all mark for one student
    @Override
    public void createAllMarkForOneStudent(String studentId) {
        //get student
        StudentEntity student = studentRepository.findById(studentId).orElseThrow(
                () -> new AppException(ErrorCode.STUDENT_NOT_FOUND)
        );

        //check exist
        if (markRepository.existsByStudentEntityStudentId( student.getStudentId() )) {
            throw new AppException(ErrorCode.MARK_EXISTED);
        }

        //get all exam of student in class
        List<ExamEntity> examEntityList = examRepository.findExamsByClassId( student.getClassEntity().getClassId() );

        //create marks
        generateMarkEntityForOneStudent(student, examEntityList);

    }

    //get all mark by examId and classId
    @Override
    public List<MarkResponse> getMarkByExamAndSessionClass(int examId, String classId) {
        //check exam
        if (!examRepository.existsById(String.valueOf(examId))) {
            throw new AppException(ErrorCode.EXAM_NOT_EXIST);
        }

        List<MarkEntity> markEntityList = markRepository.findMarksByExamIdAndClassId((long) examId, classId );
        if (!markEntityList.isEmpty()) {
            //calculate all mark of student in class
            calculateAllStudentsMarkInClass(classId);

            return toMarkResponseList(markEntityList);
        } else {
            throw new AppException(ErrorCode.MARK_LIST_EMPTY);
        }
    }

    //calculate all mark of student in class
    @Override
    public void calculateAllStudentsMarkInClass(String classId) {
        //get class
        ClassEntity classEntity = classRepository.findById(classId).orElseThrow(
                () -> new AppException(ErrorCode.CLASS_NOT_FOUND)
        );

        //get student list
        List<StudentEntity> studentEntityList = classEntity.getStudentEntityList();

        for (StudentEntity student : studentEntityList) {
            calculateAverageMark(student.getStudentId());
        }

    }

}
