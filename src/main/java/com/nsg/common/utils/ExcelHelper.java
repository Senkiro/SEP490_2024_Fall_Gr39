package com.nsg.common.utils;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.entity.*;
import com.nsg.repository.BatchRepository;
import com.nsg.repository.ClassRepository;
import com.nsg.repository.CurriculumnListRepository;
import com.nsg.repository.ExamTypeRepository;
import com.nsg.service.CurriculumnListService;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class ExcelHelper {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ExamTypeRepository examTypeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CurriculumnListRepository curriculumnListRepository;

    public static boolean hasExcelFormat(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                || contentType.equals("application/vnd.ms-excel"));
    }

    public List<StudentEntity> excelToStudents(InputStream is) {
        List<StudentEntity> students = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            String batchName = getStringCellValue(sheet.getRow(2).getCell(1)).trim();
            String className = getStringCellValue(sheet.getRow(3).getCell(1)).trim();

            if (batchName.isEmpty()) {
                throw new AppException(ErrorCode.BATCH_NOT_EXISTED);
            }
            BatchEntity batch = batchRepository.findByBatchName(batchName)
                    .orElseThrow(() -> new AppException(ErrorCode.BATCH_NOT_EXISTED));

            //check batch status
            if (batch.getBatchStatus() == 0 || batch.getBatchStatus() == 1) {
                throw new AppException(ErrorCode.BATCH_IS_CLOSED);
            }

            ClassEntity newClassEntity = new ClassEntity();
            List<ClassEntity> classes = classRepository.findByClassName(className);
            for(ClassEntity classEntity : classes) {
                if(classEntity.getBatchEntity().getBatchName().equals(batchName)) {
                    newClassEntity = classEntity;
                }
            }
            System.out.println("Batch name from Excel: '" + batchName + "'");
            System.out.println("Class name from Excel: '" + className + "'");
            System.out.println("Class name from Excel: '" + newClassEntity + "'");
            if (newClassEntity==null) {
                throw new AppException(ErrorCode.CLASS_NOT_FOUND);
            }
            ClassEntity classEntity = newClassEntity;
            System.out.println("Class found: " + classEntity.getClassName());

            Iterator<Row> rows = sheet.iterator();
            rows.next(); // Bỏ qua Batch
            rows.next(); // Bỏ qua Class
            rows.next();
            rows.next(); // Bỏ qua tiêu đề cột

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (isRowEmpty(currentRow)) {
                    continue;
                }

                UserEntity user = new UserEntity();
                String fullName = getStringCellValue(currentRow.getCell(1));
                user.setFullName(fullName);
                user.setJapaneseName(getStringCellValue(currentRow.getCell(2)));
                //check email existed?
                user.setEmail(getStringCellValue(currentRow.getCell(3)));

                user.setRole("STUDENT");
                String defaultPassword = "12341234";
                user.setPassword(passwordEncoder.encode(defaultPassword));
                user.setActive(true);
                user.setUsername(generateUsername(fullName));

                Cell dobCell = currentRow.getCell(4);
                if (dobCell != null && dobCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dobCell)) {
                    user.setDob(dobCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
                user.setGender("Male".equalsIgnoreCase(getStringCellValue(currentRow.getCell(5))));

                StudentEntity student = new StudentEntity();
                student.setUser(user);
                student.setRollNumber(generateRollNumber());
                student.setBatchEntity(batch);
                student.setClassEntity(classEntity);

                students.add(student);
            }
        } catch (AppException e) {
            // Không chuyển đổi AppException để giữ thông tin lỗi
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.PARSE_ERROR);
        }
        return students;
    }

    //upload lesson
    public Map<String, List<?>> excelToLessonsAndExams(InputStream is) {
        Map<String, List<?>> result = new HashMap<>();

        List<LessonEntity> lessons = new ArrayList<>();
        List<ExamEntity> exams = new ArrayList<>();
        List<CurriculumnEntity> curriculumns = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {

            // Kiểm tra số lượng sheet
            if (workbook.getNumberOfSheets() < 2) {
                throw new AppException(ErrorCode.NUMBER_SHEET);
            }

            // Kiểm tra và xử lý Lesson Sheet
            Sheet lessonSheet = workbook.getSheetAt(0);
            if (lessonSheet == null) {
                throw new AppException(ErrorCode.LESSON_SHEET_MISSING);
            }
            processLessonSheet(lessonSheet, lessons);

            // Kiểm tra và xử lý Exam Sheet
            Sheet examSheet = workbook.getSheetAt(1);
            if (examSheet == null) {
                throw new AppException(ErrorCode.EXAM_SHEET_MISSING);
            }
            processExamSheet(examSheet, exams);

            // Kiểm tra và xử lý Curriculumn
            if (workbook.getNumberOfSheets() > 2) {
                Sheet curriculumnSheet = workbook.getSheetAt(2);
                if (curriculumnSheet == null) {
                    throw new AppException(ErrorCode.CURRICULUMN_LIST_NOT_FOUND);
                }
                processCurriculumnSheet(curriculumnSheet, curriculumns, lessons, exams);
            }

            // Lưu kết quả vào map
            result.put("lessons", lessons);
            result.put("exams", exams);
            result.put("curriculumns", curriculumns);

        } catch (AppException e) {
            System.err.println("Error: " + e.getErrorCode().getMessage());
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.PARSE_ERROR);
        }

        return result;
    }

    private void processLessonSheet(Sheet sheet, List<LessonEntity> lessons) {
        Iterator<Row> rows = sheet.iterator();
        rows.next();
        rows.next(); // Bỏ qua tiêu đề cột

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (isRowEmpty(currentRow)) {
                continue;
            }
            LessonEntity lessonEntity = new LessonEntity();

            lessonEntity.setLessonTitle(getStringCellValue(currentRow.getCell(1)));
            lessonEntity.setVocabulary(getStringCellValue(currentRow.getCell(2)));
            lessonEntity.setKanji(getStringCellValue(currentRow.getCell(3)));
            lessonEntity.setGrammar(getStringCellValue(currentRow.getCell(4)));

            lessons.add(lessonEntity);
        }
    }

    private void processExamSheet(Sheet sheet, List<ExamEntity> exams) {
        Iterator<Row> rows = sheet.iterator();
        rows.next();
        rows.next(); // Bỏ qua tiêu đề cột

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (isRowEmpty(currentRow)) {
                continue;
            }
            ExamEntity examEntity = new ExamEntity();

            examEntity.setExamTitle(getStringCellValue(currentRow.getCell(1)));
            examEntity.setExamContent(getStringCellValue(currentRow.getCell(2)));
            String examType = getStringCellValue(currentRow.getCell(3));
            ExamTypeRateEntity examTypeRate = examTypeRepository.findByExamType(Integer.valueOf(examType)).orElseThrow(
                    () -> new AppException(ErrorCode.EXAM_TYPE_NOT_FOUND)
            );

            if (examTypeRate != null) {
                examEntity.setExamTypeRateEntity(examTypeRate);
            }

            exams.add(examEntity);
        }
    }

    private void processCurriculumnSheet(Sheet sheet,
                                         List<CurriculumnEntity> curriculumns,
                                         List<LessonEntity> lessons,
                                         List<ExamEntity> exams) {

        String curriculumnListTitle = getStringCellValue(sheet.getRow(2).getCell(1)).trim();
        CurriculumnListEntity curriculumnListEntity = curriculumnListRepository.findByCurriculumnTitle(curriculumnListTitle).orElseThrow(
                () -> new AppException(ErrorCode.CURRICULUMN_LIST_NOT_FOUND)
        );

        Iterator<Row> rows = sheet.iterator();
        rows.next();
        rows.next();
        rows.next(); // Bỏ qua tiêu đề cột

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (isRowEmpty(currentRow)) {
                continue;
            }
            CurriculumnEntity curriculumn = new CurriculumnEntity();

            String sessionNo = getStringCellValue(currentRow.getCell(0));
            String lessonTitle = getStringCellValue(currentRow.getCell(1));
            String examTitle = getStringCellValue(currentRow.getCell(2));
//            String curriculumnListId = getStringCellValue(currentRow.getCell(3));

            if (NumberUtils.isParsable(sessionNo)) {
                curriculumn.setSessionNumber(Integer.parseInt(sessionNo));
            }

            curriculumn.setCurriculumnListEntity(curriculumnListEntity);

            //find in lesson, exam list
            if (!lessonTitle.isEmpty()) {
                // Ví dụ tìm kiếm Lesson
                Optional<LessonEntity> lesson = findLessonByTitle(lessons, lessonTitle);
                lesson.ifPresentOrElse(
                        curriculumn::setLessonEntity,
                        () -> System.out.println("Lesson not found")
                );
            } else {
                curriculumn.setLessonEntity(null);
            }

            if (!examTitle.isEmpty()) {
                Optional<ExamEntity> exam = findExamByTitle(exams, examTitle);
                exam.ifPresentOrElse(
                        curriculumn::setExamEntity,
                        () -> System.out.println("Exam not found")
                );
            } else {
                curriculumn.setExamEntity(null);
            }
            curriculumns.add(curriculumn);
        }
    }


    // Phương thức tạo Roll Number
    private static String generateRollNumber() {
        String prefix = "FA";
        int year = LocalDate.now().getYear() % 100;
        int randomNumber = 1000 + new Random().nextInt(9000);
        return prefix + year + String.format("%04d", randomNumber);
    }

    private static String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : String.valueOf((int) cell.getNumericCellValue());
    }

    private static boolean isRowEmpty(Row row) {
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

    public static Optional<LessonEntity> findLessonByTitle(List<LessonEntity> lessons, String lessonTitle) {
        return lessons.stream()
                .filter(lesson -> lessonTitle.equalsIgnoreCase(lesson.getLessonTitle()))
                .findFirst();
    }

    public static Optional<ExamEntity> findExamByTitle(List<ExamEntity> exams, String examTitle) {
        return exams.stream()
                .filter(exam -> examTitle.equalsIgnoreCase(exam.getExamTitle()))
                .findFirst();
    }

    public static String generateUsername(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }

        // Loại bỏ dấu tiếng Việt
        String normalized = Normalizer.normalize(fullName, Normalizer.Form.NFD);
        String noAccent = normalized.replaceAll("\\p{M}", "");

        // Loại bỏ các ký tự không phải chữ cái hoặc dấu cách
        noAccent = noAccent.replaceAll("[^a-zA-Z\\s]", "");

        // Loại bỏ khoảng trắng thừa và viết hoa chữ cái đầu mỗi từ
        String[] words = noAccent.trim().split("\\s+");
        StringBuilder username = new StringBuilder();
        for (String word : words) {
            username.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase());
        }

        return username.toString();
    }
}

