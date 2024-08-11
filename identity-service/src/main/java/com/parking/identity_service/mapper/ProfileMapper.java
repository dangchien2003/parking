package com.parking.identity_service.mapper;

import com.parking.identity_service.dto.request.CustomerCreationRequest;
import com.parking.identity_service.dto.request.CustomerProfileCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    CustomerProfileCreationRequest toCustomerProfileCreationRequest(CustomerCreationRequest request);
}
