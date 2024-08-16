package com.parking.profile_service.controller;

import com.parking.profile_service.dto.request.CustomerProfileCreationRequest;
import com.parking.profile_service.dto.response.ApiResponse;
import com.parking.profile_service.dto.response.CustomerProfileResponse;
import com.parking.profile_service.service.CustomerProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/customer")
public class InternalCustomerProfileController {

    CustomerProfileService customerProfileService;

    @PostMapping
    ApiResponse<CustomerProfileResponse> createProfile(@RequestBody CustomerProfileCreationRequest request) {
        return ApiResponse.<CustomerProfileResponse>builder()
                .result(customerProfileService.createProfile(request))
                .build();
    }
}
