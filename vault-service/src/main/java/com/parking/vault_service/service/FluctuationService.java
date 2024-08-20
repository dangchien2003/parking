package com.parking.vault_service.service;

import com.parking.vault_service.dto.request.FlucTicketPurchaseRequest;
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

    public AddFuctuationResponse ticketPurchase(FlucTicketPurchaseRequest request) {
        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Owner owner = ownerRepository.findById(uid)
                .orElseThrow(() -> new AppException(ErrorCode.OWNER_NOT_EXIST));

        owner.setBalance(owner.getBalance() - request.getAmount());
        owner = ownerRepository.save(owner);

        Fluctuation fluctuation = fluctuationMapper.toFluctuation(request);
        fluctuation.setOwnerId(owner.getId());
        fluctuation.setTransaction(ETranSaction.DEBIT.getValue());
        fluctuation.setCreateAt(Instant.now().toEpochMilli());
        fluctuation.setDescription(EPrefixDescription.BUY_TICKET.getValue() + "-" + request.getTicketId());

        fluctuation = fluctuationRepository.save(fluctuation);

        return fluctuationMapper.toAddFuctuationResponse(fluctuation);
    }


}
