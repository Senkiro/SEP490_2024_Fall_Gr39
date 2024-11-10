package com.nsg.common.utils;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.StudentEntity;
import com.nsg.entity.UserEntity;
import com.nsg.repository.BatchRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    static BatchRepository batchRepository;

    public ExcelHelper(BatchRepository batchRepository) {
        this.batchRepository = batchRepository;
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                || contentType.equals("application/vnd.ms-excel"));
    }

    public static List<StudentEntity> excelToStudents(InputStream is) {
        List<StudentEntity> students = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            // Đọc batch và class từ ô B3
            String batchName = getStringCellValue(sheet.getRow(2) != null ? sheet.getRow(2).getCell(1) : null).trim();

            System.out.println("Batch name from Excel: '" + batchName + "'");

            if (batchName.isEmpty()) {
                System.out.println("Batch name is empty or null. Please check the Excel file.");
                throw new AppException(ErrorCode.BATCH_NOT_EXISTED);
            }

            BatchEntity batch = batchRepository.findByBatchName(batchName)
                    .orElseThrow(() -> new AppException(ErrorCode.BATCH_NOT_EXISTED));

            Iterator<Row> rows = sheet.iterator();
            rows.next(); // Bỏ qua dòng chứa Batch
            rows.next(); // Bỏ qua dòng chứa Class
            rows.next(); // Bỏ qua dòng chứa tiêu đề cột
            rows.next(); // Bỏ qua dòng đầu tiên chứa dữ liệu (dòng thứ 5)

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (currentRow == null || isRowEmpty(currentRow)) {
                    continue; // Bỏ qua hàng trống
                }

                UserEntity user = new UserEntity();
                user.setFullName(getStringCellValue(currentRow.getCell(1)));
                user.setJapaneseName(getStringCellValue(currentRow.getCell(2)));
                user.setEmail(getStringCellValue(currentRow.getCell(3)));

                // Gán role là STUDENT
                user.setRole("STUDENT");

                // Tạo Roll Number
                String rollNumber = generateRollNumber();

                // Xử lý ngày sinh và giới tính
                Cell dobCell = currentRow.getCell(4);
                if (dobCell != null && dobCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dobCell)) {
                    user.setDob(dobCell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }

                Cell genderCell = currentRow.getCell(5);
                user.setGender("Male".equalsIgnoreCase(getStringCellValue(genderCell)));

                StudentEntity student = new StudentEntity();
                student.setUser(user);
                student.setRollNumber(rollNumber); // Set Roll Number đã được tạo ra
                student.setBatchEntity(batch); // Gắn batch cho sinh viên

                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.PARSE_ERROR);
        }
        System.out.println("Total students read: " + students.size());
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

