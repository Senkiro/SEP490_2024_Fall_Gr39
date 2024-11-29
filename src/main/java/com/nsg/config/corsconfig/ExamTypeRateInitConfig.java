package com.nsg.config.corsconfig;

import com.nsg.entity.ExamTypeRateEntity;
import com.nsg.repository.ExamTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ExamTypeRateInitConfig {
    @Bean(name = "examTypeRateRunner")
    ApplicationRunner applicationRunner(ExamTypeRepository examTypeRateRepository) {
        return args -> {
            // Kiểm tra nếu bảng chưa có dữ liệu
            if (examTypeRateRepository.count() == 0) {
                // Tạo danh sách các ExamTypeRate mặc định
                ExamTypeRateEntity finalExam = ExamTypeRateEntity.builder()
                        .examType(1)
                        .examRate("10")
                        .examName("Final Exam")
                        .build();

                ExamTypeRateEntity midtermExam = ExamTypeRateEntity.builder()
                        .examType(2)
                        .examRate("10")
                        .examName("Mid-term Exam")
                        .build();

                ExamTypeRateEntity quiz = ExamTypeRateEntity.builder()
                        .examType(3)
                        .examRate("70")
                        .examName("Daily")
                        .build();

                // Lưu vào cơ sở dữ liệu
                examTypeRateRepository.saveAll(Arrays.asList(finalExam, midtermExam, quiz));
                log.info("Default ExamTypeRate data has been created successfully!");
            } else {
                log.info("ExamTypeRate data already exists. Skipping seed.");
            }
        };
    }
}
