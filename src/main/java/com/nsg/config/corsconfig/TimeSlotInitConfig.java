package com.nsg.config.corsconfig;

import com.nsg.entity.TimeSlotEntity;
import com.nsg.repository.TimeSlotRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TimeSlotInitConfig {

    @Bean(name = "timeSlotRunner")
    ApplicationRunner timeSlotRunner(TimeSlotRepository timeSlotRepository) {
        return args -> {
            if (timeSlotRepository.count() == 0) {
                // Tạo danh sách các TimeSlot mặc định
                TimeSlotEntity morningSlot = TimeSlotEntity.builder()
                        .name("Morning")
                        .startTime(LocalTime.of(7, 30))
                        .endTime(LocalTime.of(12, 30))
                        .build();

                TimeSlotEntity afternoonSlot = TimeSlotEntity.builder()
                        .name("Afternoon")
                        .startTime(LocalTime.of(13, 30))
                        .endTime(LocalTime.of(17, 30))
                        .build();

                // Lưu vào cơ sở dữ liệu
                timeSlotRepository.saveAll(Arrays.asList(morningSlot, afternoonSlot));
                log.info("Default TimeSlot data has been created successfully!");
            } else {
                log.info("TimeSlot data already exists. Skipping seed.");
            }
        };
    }
}
