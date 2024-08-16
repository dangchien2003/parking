package com.parking.vault_service.service;

import com.parking.vault_service.dto.request.DepositApproveRequest;
import com.parking.vault_service.dto.request.DepositCreationRequest;
import com.parking.vault_service.dto.response.DepositResponse;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<Fluctuation> approveDeposit(DepositApproveRequest request) {

        List<Deposit> deposits = depositRepository.findAllByIdInAndActionAtIsNull(Arrays.asList(request.getDeposits()));

        if (deposits.isEmpty())
            throw new AppException(ErrorCode.DEPOSIT_NOT_EXIST_OR_BEEN_APPROVED);

        List<Fluctuation> fluctuations = new ArrayList<>();

        long now = Instant.now().toEpochMilli();
        for (Deposit deposit : deposits) {
            Fluctuation fluctuation = fluctuationMapper.toFluctuation(deposit);
            fluctuation.setTransaction(ETranSaction.CREDIT.getValue());
            fluctuation.setDescription(
                    EPrefixDescription.APPROVE.getValue() + "-Staff approves deposit request");
            fluctuation.setCreateAt(now);

            fluctuations.add(fluctuation);

            deposit.setActionAt(now);
            deposit.setActionBy(EPersonal.STAFF.name());
        }

        depositRepository.saveAll(deposits);

        return fluctuationRepository.saveAll(fluctuations);
    }

    public DepositResponse create(DepositCreationRequest request) {

        String uid = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Owner owner = ownerRepository.findById(uid)
                .orElseThrow(() -> new AppException(ErrorCode.OWNER_NOT_EXIST));

        Deposit deposit = depositMapper.toDeposit(request);
        deposit.setOwnerId(owner.getId());
        deposit.setCreateAt(Instant.now().toEpochMilli());

        deposit = depositRepository.save(deposit);
        return depositMapper.toDepositResponse(deposit);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<Deposit> getAll(String type, int page, String sort) {

        EGetAllDeposit eGetAllDeposit = getTypeGetAll(type);

        Pageable pageable = getPageable(page, EPageAmount.DEPOSIT.getAmount(), getSort(sort, "createAt"));

        List<Deposit> deposits;

        switch (eGetAllDeposit) {
            case ANY -> deposits = depositRepository.findAll(pageable)
                    .getContent();
            case WAITING -> deposits = depositRepository.findAllByActionAt(0, pageable);
            case APPROVED -> deposits = depositRepository.findAllByActionAtGreaterThan(0, pageable);
            default -> throw new AppException(ErrorCode.UNSUPPORTED_FILTERS);
        }

        return deposits;
    }

    public List<DepositResponse> customerGetAll(String type, int page, String sort) {

        EGetAllDeposit eGetAllDeposit = getTypeGetAll(type);

        String owner = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Pageable pageable = getPageable(page, EPageAmount.DEPOSIT.getAmount(), getSort(sort, "createAt"));

        List<Deposit> deposits;

        switch (eGetAllDeposit) {
            case ANY -> deposits = depositRepository.findAll(pageable)
                    .getContent();
            case WAITING -> deposits = depositRepository.findAllByOwnerIdAndActionAt(owner, 0, pageable);
            case APPROVED -> deposits = depositRepository.findAllByOwnerIdAndActionAtGreaterThan(owner, 0, pageable);
            default -> throw new AppException(ErrorCode.UNSUPPORTED_FILTERS);
        }

        return deposits.stream()
                .map(depositMapper::toDepositResponse)
                .toList();
    }

    Sort getSort(String type, String field) {

        try {
            type = type.toUpperCase(Locale.ROOT);
        } catch (Exception e) {
            type = "DESC";
        }

        if (type.equals("ASC")) {
            return Sort.by(Sort.Order.asc(field));
        } else {
            return Sort.by(Sort.Order.desc(field));
        }
    }

    EGetAllDeposit getTypeGetAll(String type) {
        type = type.toUpperCase(Locale.ROOT);

        try {
            return EGetAllDeposit.valueOf(type);
        } catch (Exception e) {
            throw new AppException(ErrorCode.NOTFOUND_URL);
        }
    }

    Pageable getPageable(int page, int pageAmount, Sort sort) {
        --page;

        return !Objects.isNull(sort)
                ? PageRequest.of(page, pageAmount, sort)
                : PageRequest.of(page, pageAmount);
    }
}
