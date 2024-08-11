package com.parking.identity_service.mapper;

import com.parking.identity_service.dto.request.RoleCreationRequest;
import com.parking.identity_service.dto.response.RoleResponse;
import com.parking.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest request);

    RoleResponse toRoleResponse(Role request);
}
