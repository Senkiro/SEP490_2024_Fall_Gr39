package com.nsg.service.imp;


import com.nsg.Mapper.UserMapper;
import com.nsg.common.enums.UserRole;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.dto.response.staff.StudentResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.StudentEntity;
import com.nsg.entity.UserEntity;
import com.nsg.repository.StudentRepository;
import com.nsg.repository.UserRepository;
import com.nsg.service.EmailService;
import com.nsg.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

//    @Autowired
//    ErrorCode errorCode;

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
    public UserEntity userCreate(UserCreationRequest request, UserRole role) {
//        UserEntity user = new UserEntity();
        UserEntity user = userMapper.toUserEntity(request);

        //checking username is existed or not
        if (userRepository.existsByEmail(request.getEmail())){
            //if existed -> throw runtime exception
             throw new AppException(ErrorCode.USER_EXISTED);
        }

        //else: create new user
        String defaultPassword = "12341234";
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setRoles(role);
        user.setActive(true);

        return userRepository.save(user);
    }

    //create student
    @Override
    public StudentEntity studentCreate(StudentCreattionRequest request){
        StudentEntity student = new StudentEntity();
        student.setRollNumber(generateRollNumber());

        //map
        UserCreationRequest userCreationRequest = request;
        //create user
        UserEntity user = userCreate(userCreationRequest, UserRole.STUDENT);

        //set user
        student.setUser(user);

        return studentRepository.save(student);
    };

    //generate random roll number
    public String generateRollNumber(){
      String prefix = "FA";
      int year = LocalDate.now().getYear() % 100;

      Random random = new Random();
      int randomNumber = 1000 + random.nextInt(9000);

      return prefix+year+String.format("%04d", randomNumber);
    };

    @Override
    public UserEntity getUserById(String userId) {
        //return result: user, if not then throw an exception: User not found (call to class exception in package Exception)
        return userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        //return result: user, if not then throw an exception: User not found (call to class exception in package Exception)
        return userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
    }

    @Override
    public UserEntity updateUser(String userId, UserUpdateRequest request) {
        UserEntity user = getUserById(userId);

//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setGender(request.isGender());
        user.setPassword(request.getPassword());
//        user.setDob(request.getDob());
//        user.setAddress(request.getAddress());
//        user.setImg(request.getImg());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserEntity> getUserByRoles(UserRole role) {
        return userRepository.getByRoles(role);
    }

    @Override
    public List<StudentResponse> getAllStudent(){
        //get all user with role student
//        List<UserEntity> userStudent = getUserByRoles(UserRole.STUDENT);

        //find all student
        List<StudentEntity> studentEntityList = studentRepository.findAll();

        //generate list for response
        List<StudentResponse> studentListResponse = new ArrayList<>();

        //for
        for (StudentEntity student : studentEntityList) {
            StudentResponse studentResponse = new StudentResponse();
            //get,set rollNumber
            studentResponse.setRollNumber(student.getRollNumber());

            //map user to UserInforResponse
            UserInforResponse userInforResponse = UserMapper.INSTANCE.toUserInforResponse(student.getUser());
            //set user
            studentResponse.setUser(userInforResponse);

            //add to response list
            studentListResponse.add(studentResponse);
        }

        return studentListResponse;
    };

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


        //generate a new random password (8 character)
        String newpass = generateRandomPassword();
        body = body + newpass;

        System.out.println("New pass: "+newpass);

        //try:
        try{
            //update new password for user in database
            resetPassword(user, newpass);

            //add new email with new password to queue
//            emailService.sendEmail(fromEmail, toEmail, subject, body);
            emailService.queueEmail(fromEmail, toEmail, subject, body);
            return "Email sent to " + toEmail;
        }
        //catch:
        //set error
        catch (Exception e) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
//            return "Error: "+e.getMessage();
        }
    }

    @Override
    public Object search(Object request) {
        return null;
    }
}
