package com.nsg.common.utils;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.ClassEntity;
import com.nsg.entity.StudentEntity;
import com.nsg.entity.UserEntity;
import com.nsg.repository.BatchRepository;
import com.nsg.repository.ClassRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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

            List<ClassEntity> classes = classRepository.findByClassNameAndBatchEntityBatchName(className, batchName);
            System.out.println("Batch name from Excel: '" + batchName + "'");
            System.out.println("Class name from Excel: '" + className + "'");
            System.out.println("Class name from Excel: '" + classes + "'");
            if (classes.isEmpty()) {
                throw new AppException(ErrorCode.CLASS_NOT_FOUND);
            }
            ClassEntity classEntity = classes.get(0);
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
                user.setPassword("12341234");

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

