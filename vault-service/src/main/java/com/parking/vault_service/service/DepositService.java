package com.parking.vault_service.service;

import com.parking.vault_service.dto.request.DepositApproveRequest;
import com.parking.vault_service.dto.request.DepositCreationRequest;
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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public List<Fluctuation> approveDeposit(DepositApproveRequest request) {

        List<Deposit> deposits = depositRepository.findAllByIdInAndActionAtIsNull(Arrays.asList(request.getDeposits()));

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
            fluctuation.setTransaction(ETranSaction.CREDIT.getValue());
            fluctuation.setDescription(
                    EPrefixDescription.APPROVE.getValue() + "-" + note);
            fluctuation.setCreateAt(now);

            fluctuations.add(fluctuation);

            deposit.setActionAt(now);
            deposit.setActionBy(EPersonal.STAFF.name());
        }

        return fluctuations;
    }

    public DepositResponse create(DepositCreationRequest request) {

        String uid = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Owner owner = ownerRepository.findById(uid)
                .orElseThrow(() -> new AppException(ErrorCode.OWNER_NOT_EXIST));

        Deposit deposit = depositRepository.findById(request.getCode()).orElse(null);
        if (!Objects.isNull(deposit))
            throw new AppException(ErrorCode.DEPOSIT_FAIL);

        deposit = depositMapper.toDeposit(request);
        deposit.setOwnerId(owner.getId());
        deposit.setCreateAt(Instant.now().toEpochMilli());

        deposit = depositRepository.save(deposit);
        return depositMapper.toDepositResponse(deposit);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public PageResponse<Deposit> getAll(String type, int page, String sort) {

        EGetAllDeposit eGetAllDeposit;

        try {
            eGetAllDeposit = ENumUtil.getType(EGetAllDeposit.class, type);
        } catch (AppException e) {
            throw new AppException(ErrorCode.NOTFOUND_URL);
        }

        Pageable pageable = PageUtil.getPageable(page, EPageAmount.DEPOSIT.getAmount(), sort, "createAt");

        Page<Deposit> pageData;

        switch (eGetAllDeposit) {
            case ANY -> pageData = depositRepository.findAll(pageable);
            case WAITING -> pageData = depositRepository.findAllByActionAtIsNull(pageable);
            case APPROVED -> pageData = depositRepository.findAllByActionAtIsNotNull(pageable);
            default -> throw new AppException(ErrorCode.UNSUPPORTED_FILTERS);
        }

        return PageUtil.renderPageResponse(pageData.getContent(), page, pageData.getSize());
    }

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

        Pageable pageable = PageUtil.getPageable(page, EPageAmount.DEPOSIT.getAmount(), sort, "CreateAt");

        Page<Deposit> pageData;

        switch (eGetAllDeposit) {
            case ANY -> pageData = depositRepository.findAll(pageable);
            case WAITING -> pageData = depositRepository.findAllByOwnerIdAndActionAtIsNull(owner, pageable);
            case APPROVED -> pageData = depositRepository.findAllByOwnerIdAndActionAtIsNotNull(owner, pageable);
            default -> throw new AppException(ErrorCode.UNSUPPORTED_FILTERS);
        }

        List<DepositResponse> depositResponses = pageData.getContent().stream()
                .map(depositMapper::toDepositResponse)
                .toList();

        return PageUtil.renderPageResponse(depositResponses, page, pageData.getSize());
    }
}
