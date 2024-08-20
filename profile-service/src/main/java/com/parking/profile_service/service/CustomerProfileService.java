package com.parking.profile_service.service;

import com.parking.profile_service.dto.request.CustomerProfileCreationRequest;
import com.parking.profile_service.dto.request.CustomerProfileUpdateRequest;
import com.parking.profile_service.dto.response.CustomerProfileResponse;
import com.parking.profile_service.entity.ProfileCustomer;
import com.parking.profile_service.enums.EPhoneActice;
import com.parking.profile_service.exception.AppException;
import com.parking.profile_service.exception.ErrorCode;
import com.parking.profile_service.mapper.CustomerProfileMapper;
import com.parking.profile_service.repository.CustomerProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class CustomerProfileService {
    CustomerProfileRepository customerProfileRepository;
    CustomerProfileMapper customerProfileMapper;

    public void selfUpdateProfile(CustomerProfileUpdateRequest request) {
        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        ProfileCustomer profileCustomer = customerProfileRepository.findById(uid)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_EXIST));

        String oldPhone = profileCustomer.getPhone();

        customerProfileMapper.updateProfileFromRequest(request, profileCustomer);

        if (Objects.isNull(oldPhone) || !oldPhone.equals(request.getPhone())) {

            if (isPhoneExisted(request.getPhone())) {
                throw new AppException(ErrorCode.PHONE_NUMBER_EXISTED);
            }

            profileCustomer.setIsPhoneActive(EPhoneActice.NO_ACTIVE.getValue());
        }

        customerProfileRepository.save(profileCustomer);
    }

    private boolean isPhoneExisted(String phone) {

        return customerProfileRepository.countByPhone(phone) > 0 ? true : false;
    }

    public CustomerProfileResponse createProfile(CustomerProfileCreationRequest request) {

        ProfileCustomer profileCustomer = customerProfileMapper.toCustomerProfile(request);

        profileCustomer.setIsPhoneActive(EPhoneActice.NO_ACTIVE.getValue());
        profileCustomer = customerProfileRepository.save(profileCustomer);

        return customerProfileMapper.toCustomerProfileResponse(profileCustomer);
    }


    @PreAuthorize("hasAuthority('ROLE_STAFF') or #uid == authentication.name")
    public CustomerProfileResponse getProfile(String uid) {

        ProfileCustomer profileCustomer = customerProfileRepository.findById(uid)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_EXIST));

        return customerProfileMapper.toCustomerProfileResponse(profileCustomer);

    }
}
