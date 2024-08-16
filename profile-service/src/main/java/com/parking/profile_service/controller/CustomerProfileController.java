package com.parking.profile_service.controller;

import com.parking.profile_service.dto.request.AvatarUpdateRequest;
import com.parking.profile_service.dto.request.CustomerProfileUpdateRequest;
import com.parking.profile_service.dto.response.ApiResponse;
import com.parking.profile_service.dto.response.CustomerProfileResponse;
import com.parking.profile_service.service.CustomerProfileService;
import com.parking.profile_service.service.UploaderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/customer")
public class CustomerProfileController {

    CustomerProfileService customerProfileService;
    UploaderService uploaderService;

    @GetMapping("/{uid}")
    ApiResponse<CustomerProfileResponse> getProfile(@PathVariable String uid) {
        return ApiResponse.<CustomerProfileResponse>builder()
                .result(customerProfileService.getProfile(uid))
                .build();
    }

    @PatchMapping
    ApiResponse<Void> updateProfile(@Valid @RequestBody CustomerProfileUpdateRequest request) {
        customerProfileService.selfUpdateProfile(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/avatar")
    ApiResponse<Void> updateAvatar(@Valid @RequestBody AvatarUpdateRequest request) {
        uploaderService.customerUpdateAvatar(request);
        return ApiResponse.<Void>builder()
                .build();
    }
}
