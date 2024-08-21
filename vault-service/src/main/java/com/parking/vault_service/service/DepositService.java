package com.parking.vault_service.service;

import com.parking.vault_service.dto.request.DepositApproveRequest;
import com.parking.vault_service.dto.request.DepositCreationRequest;
import com.parking.vault_service.dto.request.StaffCancelDepositRequest;
import com.parking.vault_service.dto.response.DepositResponse;
import com.parking.vault_service.dto.response.PageResponse;
import com.parking.vault_service.entity.Deposit;
import com.parking.vault_service.entity.Fluctuation;
import com.parking.vault_service.entity.Owner;
import com.parking.vault_service.enums.*;
import com.parking.vault_service.exception.AppException;
import com.parking.vault_service.exception.ErrorCode;
import com.parking.vault_service.mapper.DepositMapper;
import com.parking.vault_service.mapper.FluctuationMapper;
import com.parking.vault_service.repository.DepositRepository;
import com.parking.vault_service.repository.FluctuationRepository;
import com.parking.vault_service.repository.OwnerRepository;
import com.parking.vault_service.utils.ENumUtil;
import com.parking.vault_service.utils.PageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DepositService {

    DepositRepository depositRepository;
    OwnerRepository ownerRepository;
    FluctuationRepository fluctuationRepository;
    DepositMapper depositMapper;
    FluctuationMapper fluctuationMapper;

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF')")
    public List<Fluctuation> approveDeposit(DepositApproveRequest request) {

        List<Deposit> deposits = depositRepository.findByIdInAndActionAtIsNull(Arrays.asList(request.getDeposits()));

        if (deposits.isEmpty())
            throw new AppException(ErrorCode.DEPOSIT_NOT_EXIST_OR_BEEN_APPROVED);

        return approveAction(deposits, "Staff approves deposit request");
    }

    List<Fluctuation> approveAction(List<Deposit> deposits, String note) {

        List<Fluctuation> fluctuations = getNewFluctuation(deposits, note);

        List<Owner> owners = getOwnersUpdateBalance(deposits);

        depositRepository.saveAll(deposits);
        ownerRepository.saveAll(owners);
        return fluctuationRepository.saveAll(fluctuations);
    }

    List<Owner> getOwnersUpdateBalance(List<Deposit> deposits) {

        List<String> listOwnerId = new ArrayList<>();
        Map<String, Integer> data = new HashMap<>();

        deposits.stream()
                .collect(Collectors.groupingBy(
                        Deposit::getOwnerId,
                        Collectors.summingInt(Deposit::getAmount)
                ))
                .forEach((key, value) -> {
                    listOwnerId.add(key);
                    data.put(key, value);
                });

        List<Owner> owners = ownerRepository.findAllById(listOwnerId);

        owners.forEach(owner -> {
            int totalAmountPlus = data.get(owner.getId());
            owner.setBalance(owner.getBalance() + totalAmountPlus);
        });

        return owners;
    }

    List<Fluctuation> getNewFluctuation(List<Deposit> deposits, String note) {

        List<Fluctuation> fluctuations = new ArrayList<>();

        long now = Instant.now().toEpochMilli();
        for (Deposit deposit : deposits) {
            Fluctuation fluctuation = fluctuationMapper.toFluctuation(deposit);
            fluctuation.setTransaction(ETransaction.CREDIT.getValue());
            fluctuation.setDescription(
                    EReason.APPROVE.getValue() + "-" + note);
            fluctuation.setCreateAt(now);

            fluctuations.add(fluctuation);

            deposit.setActionAt(now);
            deposit.setActionBy(EPersonal.STAFF.name());
        }

        return fluctuations;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public DepositResponse create(DepositCreationRequest request) {

        String uid = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Owner owner = ownerRepository.findById(uid)
                .orElseThrow(() -> new AppException(ErrorCode.OWNER_NOT_EXIST));

        int numDepositWaiting = depositRepository.countByOwnerIdAndCancelAtIsNullAndActionAtIsNull(owner.getId());

        if (numDepositWaiting >= 3)
            throw new AppException(ErrorCode.MANY_DEPOSIT);

        Deposit deposit = depositRepository.findById(request.getCode()).orElse(null);
        if (!Objects.isNull(deposit))
            throw new AppException(ErrorCode.DEPOSIT_FAIL);

        deposit = depositMapper.toDeposit(request);
        deposit.setOwnerId(owner.getId());
        deposit.setCreateAt(Instant.now().toEpochMilli());

        deposit = depositRepository.save(deposit);
        return depositMapper.toDepositResponse(deposit);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF')")
    public PageResponse<Deposit> getAll(String type, int page, String sort) {

        EGetAllDeposit eGetAllDeposit;

        try {
            eGetAllDeposit = ENumUtil.getType(EGetAllDeposit.class, type);
        } catch (AppException e) {
            throw new AppException(ErrorCode.NOTFOUND_URL);
        }

        Pageable pageable = PageUtil.getPageable(page, EPageQuantity.DEPOSIT.getQuantity(), sort, "createAt");

        Page<Deposit> pageData;

        switch (eGetAllDeposit) {
            case ANY -> pageData = depositRepository.findAll(pageable);
            case WAITING -> pageData = depositRepository.findAllByActionAtIsNullAndCancelAtIsNull(pageable);
            case APPROVED -> pageData = depositRepository.findAllByActionAtIsNotNull(pageable);
            case CANCELED -> pageData = depositRepository.findAllByCancelAtIsNotNull(pageable);
            default -> throw new AppException(ErrorCode.UNSUPPORTED_FILTERS);
        }

        return PageUtil.renderPageResponse(pageData.getContent(), page, pageData.getSize());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public void cancelDeposit(String id) {
        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Owner owner = ownerRepository.findById(uid)
                .orElseThrow(() -> new AppException(ErrorCode.OWNER_NOT_EXIST));

        Deposit deposit = depositRepository.findByIdAndOwnerId(id, owner.getId())
                .orElseThrow(() -> new AppException(ErrorCode.UPDATE_FAIL));

        if (!Objects.isNull(deposit.getCancelAt()) || !Objects.isNull(deposit.getActionAt()))
            throw new AppException(ErrorCode.UPDATE_FAIL);

        deposit.setCancelAt(Instant.now().toEpochMilli());
        depositRepository.save(deposit);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF')")
    public List<String> cancelDeposit(StaffCancelDepositRequest request) {
        List<Deposit> deposits = depositRepository
                .findByIdInAndActionAtIsNullAndCancelAtIsNull(
                        request.getDepositsId());

        if (deposits.isEmpty())
            throw new AppException(ErrorCode.UPDATE_FAIL);

        List<String> depositsCancel = new ArrayList<>();
        long now = Instant.now().toEpochMilli();
        deposits.forEach(deposit -> {
            deposit.setCancelAt(now);
            depositsCancel.add(deposit.getId());
        });

        depositRepository.saveAll(deposits);
        return depositsCancel;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public PageResponse<DepositResponse> customerGetAll(String type, int page, String sort) {

        EGetAllDeposit eGetAllDeposit;

        try {
            eGetAllDeposit = ENumUtil.getType(EGetAllDeposit.class, type);
        } catch (AppException e) {
            throw new AppException(ErrorCode.NOTFOUND_URL);
        }

        String owner = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Pageable pageable = PageUtil.getPageable(page, EPageQuantity.DEPOSIT.getQuantity(), sort, "CreateAt");

        Page<Deposit> pageData;

        switch (eGetAllDeposit) {
            case ANY -> pageData = depositRepository.findAll(pageable);
            case WAITING -> pageData = depositRepository.findAllByOwnerIdAndActionAtIsNull(owner, pageable);
            case APPROVED -> pageData = depositRepository.findAllByOwnerIdAndActionAtIsNotNull(owner, pageable);
            case CANCELED -> pageData = depositRepository.findAllByOwnerIdAndCancelAtIsNotNull(owner, pageable);
            default -> throw new AppException(ErrorCode.UNSUPPORTED_FILTERS);
        }

        List<DepositResponse> depositResponses = pageData.getContent().stream()
                .map(depositMapper::toDepositResponse)
                .toList();

        return PageUtil.renderPageResponse(depositResponses, page, pageData.getSize());
    }
}
