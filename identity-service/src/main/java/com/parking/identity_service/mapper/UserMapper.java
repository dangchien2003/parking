package com.parking.identity_service.mapper;

import com.parking.identity_service.dto.request.CustomerCreationRequest;
import com.parking.identity_service.dto.request.StaffCreationRequest;
import com.parking.identity_service.dto.response.UserResponse;
import com.parking.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User customerCreationRequestToUser(CustomerCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    User staffCreationRequestToUser(StaffCreationRequest request);

    UserResponse toUserResponse(User user);
}
