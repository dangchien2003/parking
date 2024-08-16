package com.parking.profile_service.mapper;

import com.parking.profile_service.dto.request.CustomerProfileCreationRequest;
import com.parking.profile_service.dto.request.CustomerProfileUpdateRequest;
import com.parking.profile_service.dto.response.CustomerProfileResponse;
import com.parking.profile_service.entity.ProfileCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerProfileMapper {
    ProfileCustomer toCustomerProfile(CustomerProfileCreationRequest request);

    void updateProfileFromRequest(CustomerProfileUpdateRequest request, @MappingTarget ProfileCustomer profileCustomer);

    CustomerProfileResponse toCustomerProfileResponse(ProfileCustomer entity);
}
