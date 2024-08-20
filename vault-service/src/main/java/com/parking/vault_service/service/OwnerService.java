package com.parking.vault_service.service;

import com.parking.vault_service.dto.request.OwnerCreationRequest;
import com.parking.vault_service.dto.response.BalanceResponse;
import com.parking.vault_service.dto.response.OwnerResponse;
import com.parking.vault_service.entity.Owner;
import com.parking.vault_service.entity.Wallet;
import com.parking.vault_service.enums.EDiscriptionUpdateWallet;
import com.parking.vault_service.enums.EWalletStatus;
import com.parking.vault_service.exception.AppException;
import com.parking.vault_service.exception.ErrorCode;
import com.parking.vault_service.mapper.OwnerMapper;
import com.parking.vault_service.repository.OwnerRepository;
import com.parking.vault_service.repository.WalletRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OwnerService {
    OwnerRepository ownerRepository;
    WalletRepository walletRepository;
    OwnerMapper ownerMapper;

    public OwnerResponse create(OwnerCreationRequest request) {
        Owner owner = ownerMapper.toOwner(request);
        owner.setCreateAt(Instant.now().toEpochMilli());

        owner = ownerRepository.save(owner);

        Wallet walletStatus = Wallet.builder()
                .ownerId(owner.getId())
                .status(EWalletStatus.ACTIVE.name())
                .description(EDiscriptionUpdateWallet.CREATE_OWNER.getDescription())
                .modifiedAt(Instant.now().toEpochMilli())
                .build();

        walletRepository.save(walletStatus);

        return ownerMapper.toOwnerCreationResponse(owner);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF')")
    public OwnerResponse getInfo(String uid) {

        Owner owner = ownerRepository.findById(uid).orElseThrow(() ->
                new AppException(ErrorCode.OWNER_NOT_EXIST));

        return ownerMapper.toOwnerCreationResponse(owner);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public BalanceResponse getBalance() {
        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Owner owner = ownerRepository.findById(uid).orElseThrow(() ->
                new AppException(ErrorCode.OWNER_NOT_EXIST));

        Wallet wallet = walletRepository.findByOwnerId(owner.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_WALLET));

        if (!wallet.getStatus().equals(EWalletStatus.ACTIVE.name()))
            throw new AppException(ErrorCode.CANNOT_USE_WALLET);

        return BalanceResponse.builder()
                .owner(uid)
                .balence(owner.getBalance())
                .build();
    }
}
