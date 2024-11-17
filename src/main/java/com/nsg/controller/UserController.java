package com.nsg.controller;

import com.nsg.dto.request.user.UserCreationRequest;
import com.nsg.dto.request.user.UserUpdateRequest;
import com.nsg.dto.response.ApiResponse;
import com.nsg.entity.UserEntity;
import com.nsg.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create-user")
    public ApiResponse<?> createUser(@RequestBody @Valid UserCreationRequest request){
        return ApiResponse.ok(userService.createUser(request, null));
    }

    //get all users in database
    @GetMapping()
    List<UserEntity> getAllUser() {
        return userService.getAllUser();
    }

    //get user by userId
    @GetMapping("/{userId}")
   public ApiResponse<?> getUser(@PathVariable("userId") String userId){
        return ApiResponse.ok(userService.getUserById(userId));
    }

    //update user
    @PutMapping("/{userId}")
    UserEntity updateUserPassword(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUserPass(userId, request);
    }

    //Delete user
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User have been deleted";
    }
}
