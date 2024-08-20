package com.parking.vault_service.service;

import com.parking.vault_service.dto.request.AdminUpdateOwnerRequest;
import com.parking.vault_service.dto.request.OwnerUpdateRequest;
import com.parking.vault_service.entity.Owner;
import com.parking.vault_service.entity.Wallet;
import com.parking.vault_service.enums.ETypeUpdateOwner;
import com.parking.vault_service.enums.EWalletStatus;
import com.parking.vault_service.exception.AppException;
import com.parking.vault_service.exception.ErrorCode;
import com.parking.vault_service.repository.OwnerRepository;
import com.parking.vault_service.repository.WalletRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class WalletService {

    OwnerRepository ownerRepository;
    WalletRepository walletRepository;

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF')")
    public List<Wallet> update(String type, AdminUpdateOwnerRequest request) {

        ETypeUpdateOwner typeUpdate = getTypeUpdate(type);

        List<Owner> owners = ownerRepository.findAllById(
                Arrays.stream(request.getOwners()).toList());

        if (CollectionUtils.isEmpty(owners)) {
            throw new AppException(ErrorCode.OWNER_NOT_EXIST);
        }

        List<Wallet> walletUpdate;

        String uid = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        switch (typeUpdate) {
            case BLOCK -> walletUpdate = blockOwner(owners, request.getDescription(), uid);
            case UNBLOCK -> walletUpdate = unblockOwner(owners, request.getDescription(), uid);
            default -> throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return walletUpdate;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public void update(String type, OwnerUpdateRequest request) {
        ETypeUpdateOwner typeUpdate = getTypeUpdate(type);

        String uid = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Owner owner = ownerRepository.findById(uid).orElseThrow(
                () -> new AppException(ErrorCode.OWNER_NOT_EXIST));


        if (Objects.requireNonNull(typeUpdate) == ETypeUpdateOwner.BLOCK) {

            Wallet wallet = walletRepository.findAll(PageRequest.
                            of(0, 1, Sort.by(
                                    Sort.Order.desc("modifiedAt"))))
                    .getContent()
                    .getFirst();

            if (wallet.getStatus()
                    .equals(EWalletStatus.FREEZE.name()))
                throw new AppException(ErrorCode.WALLET_IN_STATUS);

            blockOwner(Collections.singletonList(owner), request.getDescription(), owner.getId());
        } else {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    ETypeUpdateOwner getTypeUpdate(String type) {
        type = type.toUpperCase(Locale.ROOT);

        try {
            return ETypeUpdateOwner.valueOf(type);
        } catch (Exception e) {
            throw new AppException(ErrorCode.TYPE_NOT_EXIST);
        }
    }

    List<Wallet> blockOwner(List<Owner> owners, String description, String personUpdate) {

        List<Wallet> wallets = owners.stream().map(
                owner -> Wallet.builder()
                        .ownerId(owner.getId())
                        .description(description)
                        .status(EWalletStatus.FREEZE.name())
                        .modifiedAt(Instant.now().toEpochMilli())
                        .modifiedBy(personUpdate)
                        .build()
        ).toList();

        return walletRepository.saveAll(wallets);
    }

    List<Wallet> unblockOwner(List<Owner> owners, String description, String personUpdate) {

        List<Wallet> wallets = owners.stream().map(
                owner -> Wallet.builder()
                        .ownerId(owner.getId())
                        .description(description)
                        .status(EWalletStatus.ACTIVE.name())
                        .modifiedAt(Instant.now().toEpochMilli())
                        .modifiedBy(personUpdate)
                        .build()
        ).toList();

        return walletRepository.saveAll(wallets);
    }
}
