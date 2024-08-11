package com.parking.profile_service.controller;

import com.parking.profile_service.dto.request.CustomerProfileCreationRequest;
import com.parking.profile_service.dto.response.ApiResponse;
import com.parking.profile_service.dto.response.CustomerProfileResponse;
import com.parking.profile_service.service.CustomerProfileService;
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

    @PostMapping
    ApiResponse<CustomerProfileResponse> createProfile(@RequestBody CustomerProfileCreationRequest request) {
        return ApiResponse.<CustomerProfileResponse>builder()
                .result(customerProfileService.createProfile(request))
                .build();
    }

    @GetMapping("/{uid}")
    ApiResponse<CustomerProfileResponse> getProfile(@PathVariable String uid) {
        return ApiResponse.<CustomerProfileResponse>builder()
                .result(customerProfileService.getProfile(uid))
                .build();
    }
}
