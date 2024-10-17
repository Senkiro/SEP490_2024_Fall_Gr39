package com.nsg.service.imp;

import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.entity.UserEntity;
import com.nsg.repository.UserRepository;
import com.nsg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public UserEntity userCreate(UserCreationRequest request) {
        UserEntity user = new UserEntity();

        //checking username is existed or not
        if (userRepository.existsByUsername(request.getUsername())){
            //if existed -> throw runtime exception
             throw new AppException(ErrorCode.USER_EXISTED);
        }

        //else: create new user
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setGender(request.isGender());
        user.setPassword(request.getPassword());
        user.setDob(request.getDob());
        user.setAddress(request.getAddress());
        user.setImg(request.getImg());
        user.setEmail(request.getEmail());
        user.setRoles("CUSTOMER");
        user.setActive(true);

        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserById(String userId) {
        //return result: user, if not then throw an exception: User not found (call to class exception in package Exception)
        return userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }

    @Override
    public UserEntity updateUser(String userId, UserUpdateRequest request) {
        UserEntity user = getUserById(userId);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.isGender());
        user.setPassword(request.getPassword());
        user.setDob(request.getDob());
        user.setAddress(request.getAddress());
        user.setImg(request.getImg());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }


    @Override
    public Object create(Object request) {
        return null;
    }

    @Override
    public Object update(String uuid, Object request) {
        return null;
    }

    @Override
    public Object get(String uuid) {
        return null;
    }

    @Override
    public void delete(String uuid) {

    }

    @Override
    public Object search(Object request) {
        return null;
    }
}
