package com.parking.identity_service.service;

import com.parking.identity_service.dto.request.RoleCreationRequest;
import com.parking.identity_service.dto.response.RoleResponse;
import com.parking.identity_service.entity.Permission;
import com.parking.identity_service.entity.Role;
import com.parking.identity_service.mapper.RoleMapper;
import com.parking.identity_service.repository.PermissionRepository;
import com.parking.identity_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public RoleResponse create(RoleCreationRequest request) {

        request.setName(request.getName().toUpperCase());

        Role role = roleMapper.toRole(request);

        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void delete(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
