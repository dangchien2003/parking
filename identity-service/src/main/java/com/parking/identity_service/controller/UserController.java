package com.parking.identity_service.controller;

import com.parking.identity_service.dto.request.BlockUserRequest;
import com.parking.identity_service.dto.request.CustomerCreationRequest;
import com.parking.identity_service.dto.request.StaffCreationRequest;
import com.parking.identity_service.dto.response.ApiResponse;
import com.parking.identity_service.dto.response.UserResponse;
import com.parking.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @PostMapping("/customer/registration")
    ApiResponse<UserResponse> createCustomer(@Valid @RequestBody CustomerCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createCustomer(request))
                .build();
    }

    @PostMapping("/staff/registration")
    ApiResponse<UserResponse> createStaff(@Valid @RequestBody StaffCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createStaff(request))
                .build();
    }

    @PatchMapping("/block")
    ApiResponse<List<UserResponse>> blockUser(@Valid @RequestBody BlockUserRequest request) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.blockAccountById(request))
                .build();
    }

}
