package com.nsg.service.imp;

import com.nsg.Mapper.UserMapper;
import com.nsg.common.enums.UserRole;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.UserEntity;
import com.nsg.repository.TeacherRepository;
import com.nsg.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImp implements TeacherService {

    @Autowired
    private final TeacherRepository teacherRepository;

    @Override
    public List<UserEntity> getAllTeachers() {
        return teacherRepository.findByRoles(UserRole.TEACHER);
    }

    @Override
    public Page<UserInforResponse> getTeachers(int page, int size) {
        Page<UserEntity> result = teacherRepository.findByRoles(UserRole.TEACHER, PageRequest.of(page, size));

        List<UserInforResponse> responseList = new ArrayList<>();

        for (UserEntity user : result) {
            UserInforResponse temp = UserMapper.INSTANCE.toUserInforResponse(user);
            responseList.add(temp);
        }

        return new PageImpl<>(responseList, result.getPageable(), result.getTotalElements());
    }

}