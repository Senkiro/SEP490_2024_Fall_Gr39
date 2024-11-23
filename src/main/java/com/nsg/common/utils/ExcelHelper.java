package com.nsg.common.utils;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.entity.*;
import com.nsg.repository.BatchRepository;
import com.nsg.repository.ClassRepository;
import com.nsg.repository.ExamTypeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
                user.setFullName(getStringCellValue(currentRow.getCell(1)));
                user.setJapaneseName(getStringCellValue(currentRow.getCell(2)));
                user.setEmail(getStringCellValue(currentRow.getCell(3)));
                user.setRole("STUDENT");
                String defaultPassword = "12341234";
                user.setPassword(passwordEncoder.encode(defaultPassword));
                user.setActive(true);

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
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.PARSE_ERROR);
        }
        return students;
    }

    //upload lesson
    public List<LessonEntity> excelToLessons(InputStream is) {
        List<LessonEntity> lessons = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {

            if (workbook.getNumberOfSheets() == 0) {
                throw new IllegalStateException("Excel file has no sheets");
            }

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalStateException("Sheet is null");
            }

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
                LessonEntity lessonEntity = new LessonEntity();

                lessonEntity.setLessonTitle(getStringCellValue(currentRow.getCell(1)));
                lessonEntity.setVocabulary(getStringCellValue(currentRow.getCell(2)));
                lessonEntity.setKanji(getStringCellValue(currentRow.getCell(3)));
                lessonEntity.setGrammar(getStringCellValue(currentRow.getCell(4)));

                lessons.add(lessonEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            throw new AppException(ErrorCode.PARSE_ERROR);
        }
        return lessons;
    }

    //upload exam
    public List<ExamEntity> excelToExams(InputStream is) {
        List<ExamEntity> exams = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {

            if (workbook.getNumberOfSheets() == 0) {
                throw new IllegalStateException("Excel file has no sheets");
            }

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new IllegalStateException("Sheet is null");
            }

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
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            throw new AppException(ErrorCode.PARSE_ERROR);
        }
        return exams;
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
}

