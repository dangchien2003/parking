package com.parking.identity_service.service;

import com.parking.identity_service.dto.request.PermissionCreationRequest;
import com.parking.identity_service.dto.response.PermissionResponse;
import com.parking.identity_service.entity.Permission;
import com.parking.identity_service.exception.AppException;
import com.parking.identity_service.exception.ErrorCode;
import com.parking.identity_service.mapper.PermissionMapper;
import com.parking.identity_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;


    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public PermissionResponse create(PermissionCreationRequest request) {

        request.setName(request.getName().toUpperCase());

        Permission permission = permissionMapper.toPermission(request);

        permission = permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<PermissionResponse> getAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void delete(String namePermission) {
        try {
            permissionRepository.deleteById(namePermission);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }
}
