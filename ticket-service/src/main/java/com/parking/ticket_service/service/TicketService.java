package com.parking.ticket_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.ticket_service.dto.request.AddFluctuationRequest;
import com.parking.ticket_service.dto.request.BuyTicketRequest;
import com.parking.ticket_service.dto.request.TicketUpdatePlateRequest;
import com.parking.ticket_service.dto.response.ApiResponse;
import com.parking.ticket_service.dto.response.BalenceResponse;
import com.parking.ticket_service.dto.response.TicketResponse;
import com.parking.ticket_service.entity.Category;
import com.parking.ticket_service.entity.CategoryHistory;
import com.parking.ticket_service.entity.Ticket;
import com.parking.ticket_service.enums.ECategoryStatus;
import com.parking.ticket_service.enums.ECategoryUnit;
import com.parking.ticket_service.exception.AppException;
import com.parking.ticket_service.exception.ErrorCode;
import com.parking.ticket_service.mapper.TicketMapper;
import com.parking.ticket_service.repository.CategoryHistoryRepository;
import com.parking.ticket_service.repository.CategoryRepository;
import com.parking.ticket_service.repository.TicketRepository;
import com.parking.ticket_service.repository.httpclient.VaultClient;
import com.parking.ticket_service.utils.ENumUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class TicketService {

    TicketRepository ticketRepository;
    CategoryRepository categoryRepository;
    CategoryHistoryRepository categoryHistoryRepository;
    TicketMapper ticketMapper;
    VaultClient vaultClient;
    ObjectMapper objectMapper;

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public void cancel(String ticketId) {
        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Ticket ticket = ticketRepository.findByIdAndUid(ticketId, uid)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        if (ticket.getTurnTotal() > 0 ||
                ticket.getCancleAt() > 0 ||
                !Objects.isNull(ticket.getContentPlate()))
            throw new AppException(ErrorCode.CANNOT_CANCEL_TICKET);


        AddFluctuationRequest addFluctuationRequest = AddFluctuationRequest.builder()
                .amount(ticket.getCategory().getPrice())
                .objectId(ticketId)
                .build();
        vaultClient.ticketCancel(addFluctuationRequest);

        ticket.setCancleAt(Instant.now().toEpochMilli());
        ticketRepository.save(ticket);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public TicketResponse updatePlate(TicketUpdatePlateRequest request) {
        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Ticket ticket = ticketRepository.findByIdAndUid(request.getTicketId(), uid)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        if ((ticket.getTurnTotal() > 0 && !Objects.isNull(ticket.getContentPlate())) ||
                ticket.getCancleAt() > 0
        )
            throw new AppException(ErrorCode.CANNOT_UPDATE_PLATE);

        ticket.setContentPlate(request.getPlate());

        return ticketMapper.toTicketResponse(ticketRepository.save(ticket));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public TicketResponse buy(BuyTicketRequest request) throws JsonProcessingException {

        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Category category = categoryRepository.findById(request.getCategory())
                .orElse(null);

        if (Objects.isNull(category) || !category.getStatus().equals(ECategoryStatus.ACTIVE.name()))
            throw new AppException(ErrorCode.INVALID_CATEGORY);
        long expire;
        CategoryHistory history;
        try {
            history = categoryHistoryRepository.findAllByCategoryOrderByCreateAtDesc(category)
                    .getFirst();

            if (!history.getStatus().equals(ECategoryStatus.ACTIVE.name()))
                throw new AppException(ErrorCode.DATA_NOT_FOUND);

            expire = getExpireTicket(history.getUnit());
        } catch (Exception e) {
            log.error("Buy fail: ", e);
            throw new AppException(ErrorCode.CANNOT_BUY_TICKET);
        }

        ApiResponse<BalenceResponse> balanceResponse = vaultClient.getBalance();

        if (balanceResponse.getResult().getBalence() < history.getPrice())
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);

        String ticketId = UUID.randomUUID().toString();

        AddFluctuationRequest addFluctuationRequest = AddFluctuationRequest.builder()
                .objectId(ticketId)
                .amount(history.getPrice())
                .build();
        vaultClient.ticketPurchase(addFluctuationRequest);

        Ticket ticket = Ticket.builder()
                .id(ticketId)
                .uid(uid)
                .category(history)
                .buyAt(Instant.now().toEpochMilli())
                .expireAt(expire)
                .build();

        ticket = ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(ticket);
    }

    long getExpireTicket(String unit) {

        ECategoryUnit typeUnit;
        try {
            typeUnit = ENumUtils.getType(ECategoryUnit.class, unit);
        } catch (AppException e) {
            throw new AppException(ErrorCode.CANNOT_BUY_TICKET);
        }

        ChronoUnit chronoUnit;
        switch (typeUnit) {
            case DAY, TIMES -> chronoUnit = ChronoUnit.DAYS;
            case WEEK -> chronoUnit = ChronoUnit.WEEKS;
            case MONTH -> chronoUnit = ChronoUnit.MONTHS;
            default -> throw new AppException(ErrorCode.NOTFOUND_CATEGORY_UNIT);
        }

        return Instant.now().plus(1, chronoUnit).toEpochMilli();
    }

}
