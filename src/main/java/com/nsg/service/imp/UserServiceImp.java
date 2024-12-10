package com.nsg.service.imp;


import com.nsg.Mapper.UserMapper;
import com.nsg.common.enums.UserRole;
import com.nsg.common.exception.AppException;
import com.nsg.common.exception.ErrorCode;
import com.nsg.dto.request.student.StudentCreattionRequest;
import com.nsg.dto.request.user.ResetPasswordRequest;
import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserInforUpdateRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.dto.response.news.NewsResponse;
import com.nsg.dto.response.user.UserInforResponse;
import com.nsg.entity.BatchEntity;
import com.nsg.entity.NewEntity;
import com.nsg.entity.StudentEntity;
import com.nsg.entity.UserEntity;
import com.nsg.repository.BatchRepository;
import com.nsg.repository.StudentRepository;
import com.nsg.repository.UserRepository;
import com.nsg.service.EmailService;
import com.nsg.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


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
    BatchRepository batchRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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

    //get user by role
    @Override
    public Page<UserInforResponse> getUsersByRoles(UserRole role, int page, int size) {

        Page<UserEntity> userEntities = userRepository.findByRoles(role, PageRequest.of(page, size));

        List<UserInforResponse> responseList = toUserInforResponseList(userEntities.getContent());

        return new PageImpl<>(responseList, userEntities.getPageable(), userEntities.getTotalElements());

    }

    public List<UserInforResponse> toUserInforResponseList(List<UserEntity> userEntities) {
        List<UserInforResponse> responseList = new ArrayList<>();

        for (UserEntity user : userEntities) {
            UserInforResponse response = UserMapper.INSTANCE.toUserInforResponse(user);
            responseList.add(response);
        }

        return responseList;
    }

    //set active to false
    @Override
    public void changeUsersActive(String userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        boolean currentActive = user.isActive();
        user.setActive(!currentActive);

        userRepository.save(user);
    }

    @Override
    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public UserEntity createUser(UserCreationRequest request, UserRole role) {
//        UserEntity user = new UserEntity();
        UserEntity user = userMapper.toUserEntity(request);

        //checking username is existed or not
        if (userRepository.existsByEmail(request.getEmail())){
            //if existed -> throw app exception
             throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        //else: create new user
        String defaultPassword = "12341234";
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setRoles(role);
        user.setActive(true);

        //set full name
        user.setUsername(generateUsername(request.getFullName()));

        return userRepository.save(user);
    }

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
                () -> new AppException(ErrorCode.EMAIL_NOT_EXISTED)
        );
    }

    @Override
    public UserEntity updateUserPass(String userId, UserUpdateRequest request) {
        UserEntity user = getUserById(userId);
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }

    //get one user information by id
    @Override
    public UserInforResponse getUserInforById(String userId){
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        UserInforResponse userInforResponse = UserMapper.INSTANCE.toUserInforResponse(user);

        return userInforResponse;
    }


    //update user information
    @Override
    public UserInforResponse updateUserInfor(String userId, UserInforUpdateRequest request, MultipartFile avatar){
        UserEntity user = getUserById(userId);

        // Cập nhật thông tin người dùng từ request
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getJapaneseName() != null) {
            user.setJapaneseName(request.getJapaneseName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        // Xử lý file ảnh (nếu có)
        if (avatar != null && !avatar.isEmpty()) {
            String savedImagePath = saveImageToAssetsFolder(avatar);
            user.setImg(savedImagePath);
        }

        // Lưu người dùng đã cập nhật
        UserEntity updatedUser = userRepository.save(user);

        // Ánh xạ kết quả thành UserInforResponse để trả về
        UserInforResponse response = new UserInforResponse();

        response.setFullName(updatedUser.getFullName());
        response.setJapaneseName(updatedUser.getJapaneseName());
        response.setEmail(updatedUser.getEmail());
        response.setDob(updatedUser.getDob());
        response.setPhone(updatedUser.getPhone());
        response.setGender(updatedUser.isGender());
        response.setImg(updatedUser.getImg());

        return response;
    }

    @Override
    public UserInforResponse updateUserPassword (ResetPasswordRequest request) {
        String userId = request.getUserId();
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);

        userRepository.save(user);

        return UserMapper.INSTANCE.toUserInforResponse(user);
    };

    private String saveImageToAssetsFolder(MultipartFile image) {
        try {
            // Đường dẫn thư mục lưu trữ ảnh
            String uploadDir = System.getProperty("user.dir") + "/frontend/public/avatar_image/";
            File dir = new File(uploadDir);

            // Tạo thư mục nếu chưa tồn tại
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Lưu ảnh vào thư mục
            String filePath = uploadDir + image.getOriginalFilename();
            File file = new File(filePath);
            image.transferTo(file);

            // Trả về đường dẫn tương đối để Vue sử dụng
            return "avatar_image/" + image.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
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

        //mapper UserEntity to userUpdateRequest
        UserUpdateRequest userUpdateRequest = userMapper.toUserUpdateRequest(user);

        //encode new password
        //encode password by Bcrypt
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String encodedPassword = passwordEncoder.encode(newpass);

        //set new pass
        userUpdateRequest.setPassword(encodedPassword);

        //update user with new password
        UserEntity userUpdate = updateUserPass(user.getUserId(), userUpdateRequest);

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

    //create userName base on fullName
    public static String generateUsername(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }

        // Loại bỏ dấu tiếng Việt
        String normalized = Normalizer.normalize(fullName, Normalizer.Form.NFD);
        String noAccent = normalized.replaceAll("\\p{M}", "");

        // Loại bỏ các ký tự không phải chữ cái hoặc dấu cách
        noAccent = noAccent.replaceAll("[^a-zA-Z\\s]", "");

        // Loại bỏ khoảng trắng thừa và viết hoa chữ cái đầu mỗi từ
        String[] words = noAccent.trim().split("\\s+");
        StringBuilder username = new StringBuilder();
        for (String word : words) {
            username.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase());
        }

        return username.toString();
    }
}
