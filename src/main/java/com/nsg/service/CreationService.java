package com.nsg.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationService {
    @Autowired
    final AttendanceService attendanceService;

    @Autowired
    final MarkService markService;

    final BlockingQueue<String> creationQueue = new LinkedBlockingQueue<>();

    public CreationService(AttendanceService attendanceService, MarkService markService) {
        this.attendanceService = attendanceService;
        this.markService = markService;
        startCreationThread();
    }

    //generate attendance and mark
    private void startCreationThread() {
        Thread creationThread = new Thread(() -> {
            while (true) {
                try {
                    // Lấy một tác vụ từ hàng đợi
                    String classId = creationQueue.take();

                    // Xử lý tác vụ
                    attendanceService.createAttendancesForSession(classId);
                    markService.generateMarkForAllStudentInClass(classId);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Xử lý trường hợp thread bị ngắt
                } catch (Exception ex) {
                    System.err.println("Error while processing task: " + ex.getMessage());
                }
            }
        });

        creationThread.setDaemon(true); // Đặt thread là daemon để nó không giữ JVM chạy khi ứng dụng tắt
        creationThread.start(); // Bắt đầu worker thread
    }

    //add queue
    public void addCreationQueue(String classId) {
        creationQueue.offer(classId);
    }

}
