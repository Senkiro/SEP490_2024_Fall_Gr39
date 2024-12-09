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
            System.out.println("Creation thread started."); // Log để kiểm tra thread bắt đầu

            while (true) {
                try {
                    String classId = creationQueue.take(); // Lấy tác vụ từ hàng đợi
                    System.out.println("Processing classId: " + classId); // Log kiểm tra dữ liệu

                    attendanceService.createAttendancesForSession(classId);
                    markService.generateMarkForAllStudentInClass(classId);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Thread bị ngắt
                    System.err.println("Thread interrupted: " + e.getMessage());
                } catch (Exception ex) {
                    System.err.println("Error while processing task: " + ex.getMessage());
                }
            }
        });

        creationThread.setDaemon(true);
        creationThread.start(); // Bắt đầu thread
        System.out.println("Thread creation started successfully."); // Log thread khởi động
    }

    //add queue
    public void addCreationQueue(String classId) {
        creationQueue.offer(classId);
    }

}
