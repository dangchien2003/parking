package com.parking.profile_service.mapper;

import com.parking.profile_service.dto.request.CustomerProfileCreationRequest;
import com.parking.profile_service.dto.response.CustomerProfileResponse;
import com.parking.profile_service.entity.CustomerProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerProfileMapper {
    CustomerProfile toCustomerProfile(CustomerProfileCreationRequest request);

    CustomerProfileResponse toCustomerProfileResponse(CustomerProfile entity);
}
