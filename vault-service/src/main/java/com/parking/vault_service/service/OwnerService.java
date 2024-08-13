package com.parking.vault_service.service;

import com.parking.vault_service.dto.request.OwnerCreationRequest;
import com.parking.vault_service.dto.response.OwnerResponse;
import com.parking.vault_service.entity.Owner;
import com.parking.vault_service.enums.EOwnerStatus;
import com.parking.vault_service.exception.AppException;
import com.parking.vault_service.exception.ErrorCode;
import com.parking.vault_service.mapper.OwnerMapper;
import com.parking.vault_service.repository.OwnerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OwnerService {
    OwnerRepository ownerRepository;
    OwnerMapper ownerMapper;

    public OwnerResponse create(OwnerCreationRequest request) {
        Owner owner = ownerMapper.toOwner(request);
        owner.setStatus(EOwnerStatus.ACTIVE.name());

        owner = ownerRepository.save(owner);

        return ownerMapper.toOwnerCreationResponse(owner);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public OwnerResponse getOwner(String uid) {

        Owner owner = ownerRepository.findById(uid).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        return ownerMapper.toOwnerCreationResponse(owner);
    }
}
