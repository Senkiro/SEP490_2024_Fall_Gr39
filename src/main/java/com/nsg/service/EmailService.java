package com.nsg.service;

import com.nsg.dto.request.email.EmailRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    private BlockingQueue<EmailRequest> emailQueue = new LinkedBlockingQueue<>();

    // Khởi chạy một luồng riêng để xử lý hàng đợi
    @PostConstruct
    public void init() {
        Thread emailSenderThread = new Thread(() -> {
            while (true) {
                try {
                    // Lấy yêu cầu từ hàng đợi và gửi email
                    EmailRequest request = emailQueue.take();
                    sendEmail(request);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        emailSenderThread.setDaemon(true); // Đảm bảo luồng sẽ dừng khi ứng dụng dừng
        emailSenderThread.start();
    }

    // Thêm yêu cầu vào hàng đợi
    public void queueEmail(String fromEmail, String toEmail, String subject, String body) {
        EmailRequest request = new EmailRequest(fromEmail, toEmail, subject, body);
        emailQueue.offer(request);
        System.out.println("Email request queued for " + toEmail);
    }

    public void sendEmail(EmailRequest request) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(request.getFromEmail());
        message.setTo(request.getToEmail());
        message.setSubject(request.getSubject());
        message.setText(request.getBody());

        mailSender.send(message);
        System.out.println("Email sent successfully to " + request.getToEmail());
    }

}
