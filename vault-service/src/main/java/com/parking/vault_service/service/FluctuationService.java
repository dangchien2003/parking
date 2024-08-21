package com.parking.vault_service.service;

import com.parking.vault_service.dto.request.AddFluctuationRequest;
import com.parking.vault_service.dto.response.PageResponse;
import com.parking.vault_service.entity.Fluctuation;
import com.parking.vault_service.entity.Owner;
import com.parking.vault_service.enums.EPageQuantity;
import com.parking.vault_service.enums.EReason;
import com.parking.vault_service.enums.ETransaction;
import com.parking.vault_service.exception.AppException;
import com.parking.vault_service.exception.ErrorCode;
import com.parking.vault_service.mapper.FluctuationMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FluctuationService {

    FluctuationRepository fluctuationRepository;
    OwnerRepository ownerRepository;
    FluctuationMapper fluctuationMapper;

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public void ticketCancel(AddFluctuationRequest request) {
        String description = EReason.CANCEL_TICKET.getValue() + "-" + request.getObjectId();
        addFluctuation(request.getAmount(), ETransaction.CREDIT, EReason.CANCEL_TICKET, description);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public void ticketPurchase(AddFluctuationRequest request) {
        String description = EReason.BUY_TICKET.getValue() + "-" + request.getObjectId();
        Fluctuation fluctuation = addFluctuation(request.getAmount(), ETransaction.DEBIT, EReason.BUY_TICKET, description);
        fluctuationMapper.toAddFuctuationResponse(fluctuation);
    }

    Fluctuation addFluctuation(int amount, ETransaction transaction, EReason reason, String description) {
        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Owner owner = ownerRepository.findById(uid)
                .orElseThrow(() -> new AppException(ErrorCode.OWNER_NOT_EXIST));

        if (transaction.equals(ETransaction.CREDIT)) {
            owner.setBalance(owner.getBalance() + amount);
        } else if (transaction.equals(ETransaction.DEBIT)) {
            owner.setBalance(owner.getBalance() - amount);
        } else {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        Fluctuation fluctuation = Fluctuation.builder()
                .ownerId(owner.getId())
                .amount(amount)
                .transaction(transaction.getValue())
                .reason(reason.getValue())
                .createAt(Instant.now().toEpochMilli())
                .description(description)
                .build();


        fluctuation = fluctuationRepository.save(fluctuation);
        ownerRepository.save(owner);
        return fluctuation;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF')")
    public PageResponse<Fluctuation> getAllByStaff(String type, int page, String sort, String field) {
        return getAll(type, null, page, sort, field);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF')")
    public PageResponse<Fluctuation> getAllByStaff(String type, String uid, int page, String sort, String field) {
        return getAll(type, uid, page, sort, field);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public PageResponse<Fluctuation> getAllByCustomer(String type, int page, String sort, String field) {
        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return getAll(type, uid, page, sort, field);
    }

    PageResponse<Fluctuation> getAll(String type, String uid, int page, String sort, String field) {
        EReason reason = null;

        if (!type.equalsIgnoreCase("OTHER")) {
            try {
                reason = ENumUtil.getType(EReason.class, type);
            } catch (AppException e) {
                throw new AppException(ErrorCode.NOTFOUND_URL);
            }
        }

        Owner owner = null;
        if (!Objects.isNull(uid)) {
            owner = ownerRepository.findById(uid)
                    .orElseThrow(() -> new AppException(ErrorCode.OWNER_NOT_EXIST));
        }

        Pageable pageable = PageUtil.getPageable(page, EPageQuantity.FLUCTUATION.getQuantity(), sort, field);

        Page<Fluctuation> pageData;

        if (!Objects.isNull(owner)) {
            if (Objects.isNull(reason)) {
                pageData = fluctuationRepository.findAllByReasonNotInAndOwnerId(getAllTypeReason(), owner.getId(), pageable);
            } else {
                pageData = fluctuationRepository.findAllByReasonAndOwnerId(reason.getValue(), owner.getId(), pageable);
            }
        } else {
            if (Objects.isNull(reason)) {
                pageData = fluctuationRepository.findAllByReasonNotIn(getAllTypeReason(), pageable);
            } else {
                pageData = fluctuationRepository.findAllByReason(reason.getValue(), pageable);
            }
        }

        return PageResponse.<Fluctuation>builder()
                .pageSize(pageData.getSize())
                .currentPage(page)
                .data(pageData.getContent())
                .build();
    }

    List<String> getAllTypeReason() {
        List<String> allType = new ArrayList<>();
        for (EReason reason : EReason.values())
            allType.add(reason.getValue());

        return allType;
    }

}
