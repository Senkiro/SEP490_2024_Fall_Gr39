package com.nsg.service.imp;

import com.nsg.common.enums.UserRole;
import com.nsg.entity.UserEntity;
import com.nsg.repository.TeacherRepository;
import com.nsg.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
    public Page<UserEntity> getTeachers(int page, int size) {
        Page<UserEntity> result = teacherRepository.findByRoles(UserRole.TEACHER, PageRequest.of(page, size));
        return result;
    }

}