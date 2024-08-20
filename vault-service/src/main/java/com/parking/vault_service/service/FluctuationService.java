package com.parking.vault_service.service;

import com.parking.vault_service.dto.request.AddFluctuationRequest;
import com.parking.vault_service.dto.response.AddFuctuationResponse;
import com.parking.vault_service.entity.Fluctuation;
import com.parking.vault_service.entity.Owner;
import com.parking.vault_service.enums.EPrefixDescription;
import com.parking.vault_service.enums.ETranSaction;
import com.parking.vault_service.exception.AppException;
import com.parking.vault_service.exception.ErrorCode;
import com.parking.vault_service.mapper.FluctuationMapper;
import com.parking.vault_service.repository.FluctuationRepository;
import com.parking.vault_service.repository.OwnerRepository;
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
public class FluctuationService {

    FluctuationRepository fluctuationRepository;
    OwnerRepository ownerRepository;
    FluctuationMapper fluctuationMapper;

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public void ticketCancel(AddFluctuationRequest request) {
        String description = EPrefixDescription.CANCEL_TICKET.getValue() + "-" + request.getObjectId();
        addFluctuation(request.getAmount(), ETranSaction.CREDIT, description);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public AddFuctuationResponse ticketPurchase(AddFluctuationRequest request) {
        String description = EPrefixDescription.BUY_TICKET.getValue() + "-" + request.getObjectId();
        Fluctuation fluctuation = addFluctuation(request.getAmount(), ETranSaction.DEBIT, description);
        return fluctuationMapper.toAddFuctuationResponse(fluctuation);
    }

    Fluctuation addFluctuation(int amount, ETranSaction tranSaction, String description) {
        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Owner owner = ownerRepository.findById(uid)
                .orElseThrow(() -> new AppException(ErrorCode.OWNER_NOT_EXIST));

        if (tranSaction.equals(ETranSaction.CREDIT)) {
            owner.setBalance(owner.getBalance() + amount);
        } else if (tranSaction.equals(ETranSaction.DEBIT)) {
            owner.setBalance(owner.getBalance() - amount);
        } else {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        Fluctuation fluctuation = Fluctuation.builder()
                .ownerId(owner.getId())
                .amount(amount)
                .transaction(tranSaction.getValue())
                .createAt(Instant.now().toEpochMilli())
                .description(description)
                .build();


        fluctuation = fluctuationRepository.save(fluctuation);
        ownerRepository.save(owner);
        return fluctuation;
    }

}
