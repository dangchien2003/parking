package com.parking.ticket_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.parking.ticket_service.dto.request.BuyTicketRequest;
import com.parking.ticket_service.dto.request.TicketUpdatePlateRequest;
import com.parking.ticket_service.dto.response.ApiResponse;
import com.parking.ticket_service.dto.response.TicketResponse;
import com.parking.ticket_service.service.TicketService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketController {

    TicketService ticketService;

    @PostMapping
    ApiResponse<TicketResponse> buy(@Valid @RequestBody BuyTicketRequest request) throws JsonProcessingException {

        return ApiResponse.<TicketResponse>builder()
                .result(ticketService.buy(request))
                .build();
    }

    @PutMapping("/plate")
    ApiResponse<TicketResponse> updatePlate(@Valid @RequestBody TicketUpdatePlateRequest request) {
        return ApiResponse.<TicketResponse>builder()
                .result(ticketService.updatePlate(request))
                .build();
    }

    @PutMapping("/cancel/{ticketId}")
    ApiResponse<Void> cancelTicket(@PathVariable(name = "ticketId", required = true) String ticket) {
        ticketService.cancel(ticket);
        return ApiResponse.<Void>builder()
                .build();
    }
}
