package com.nsg.service.imp;


import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.entity.UserEntity;
import com.nsg.repository.UserRepository;
import com.nsg.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    UserMapper userMapper;

    //generate random string (8 characters)
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 8;

    public static String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

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
    public UserEntity getUserByEmail(String email) {
        //return result: user, if not then throw an exception: User not found (call to class exception in package Exception)
        return userRepository.findByEmail(email).orElseThrow(
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
    public String resetPassword(UserEntity user, String newpass) {
        //receive a UserEnity, a string of new password

        //mapper UserEntity to userUpdateRequest
        UserUpdateRequest userUpdateRequest = userMapper.toUserUpdateRequest(user);

        //encode new password
        //encode password by Bcrypt
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String encodedPassword = passwordEncoder.encode(newpass);


        //set new pass
        userUpdateRequest.setPassword(encodedPassword);

        //update user with new password
        UserEntity userUpdate = updateUser(user.getUserId(), userUpdateRequest);

        String result = "";

        if (userUpdate == null){
            result = "reset password success!";
        }else {
            result = "reset password fail!";
        }

        //return result
        return result;
    }

    @Override
    public String forgetPassword(UserEntity user, String toEmail) {

        //set content for email
        String fromEmail = "hoangson00as@gmail.com";
        String subject = "New password for your account";
        String body = "Your new password is: ";

        //validate email

        //generate a new random password (8 character)
        String newpass = generateRandomPassword();
        body = body + newpass;

        System.out.println("New pass: "+newpass);

        //try:
        try{
            //update new password for user in database
            resetPassword(user, newpass);

            //send email with new password to email
            emailService.sendEmail(fromEmail, toEmail, subject, body);
            return "Email sent to " + toEmail;
        }
        //catch:
        //set error
        catch (Exception e) {

            return "Error: "+e.getMessage();
        }
    }

    @Override
    public Object search(Object request) {
        return null;
    }
}
