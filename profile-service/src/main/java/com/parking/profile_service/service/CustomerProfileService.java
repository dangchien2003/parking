package com.parking.profile_service.service;

import com.parking.profile_service.dto.request.CustomerProfileCreationRequest;
import com.parking.profile_service.dto.response.CustomerProfileResponse;
import com.parking.profile_service.entity.CustomerProfile;
import com.parking.profile_service.enums.EPhoneActice;
import com.parking.profile_service.exception.AppException;
import com.parking.profile_service.exception.ErrorCode;
import com.parking.profile_service.mapper.CustomerProfileMapper;
import com.parking.profile_service.repository.CustomerProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class CustomerProfileService {
    CustomerProfileRepository customerProfileRepository;
    CustomerProfileMapper customerProfileMapper;

    public CustomerProfileResponse createProfile(CustomerProfileCreationRequest request) {

        CustomerProfile customerProfile = customerProfileMapper.toCustomerProfile(request);

        customerProfile.setIsPhoneActive(EPhoneActice.NO_ACTIVE.getValue());
        customerProfile = customerProfileRepository.save(customerProfile);

        return customerProfileMapper.toCustomerProfileResponse(customerProfile);

    }

    public CustomerProfileResponse getProfile(String uid) {

        CustomerProfile customerProfile = customerProfileRepository.findById(uid)
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        return customerProfileMapper.toCustomerProfileResponse(customerProfile);

    }
}
