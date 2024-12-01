package com.nsg.service;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public interface TeacherService {
    //get all teacher
    List<UserEntity> getAllTeachers();
    //get teacher paginate
    Page<UserInforResponse> getTeachers(int page, int size);
}