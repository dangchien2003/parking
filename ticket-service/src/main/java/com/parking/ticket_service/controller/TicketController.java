package com.parking.ticket_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.parking.ticket_service.dto.request.*;
import com.parking.ticket_service.dto.response.ApiResponse;
import com.parking.ticket_service.dto.response.InfoTicketResponse;
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
    ApiResponse<TicketResponse> buy(@Valid @RequestBody BuyTicketRequest request) {
        return ApiResponse.<TicketResponse>builder()
                .result(ticketService.buy(request))
                .build();
    }

    @GetMapping("/{ticket}")
    ApiResponse<InfoTicketResponse> info(@PathVariable(name = "ticket") String ticket) {
        return ApiResponse.<InfoTicketResponse>builder()
                .result(ticketService.getInfoTicket(ticket))
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

    @PatchMapping("/extend/{ticketId}")
    ApiResponse<Void> extendTicket(@PathVariable(name = "ticketId") String ticket,
                                   @RequestParam(name = "date") String date,
                                   @RequestParam(name = "time") String time) {
        ticketService.extendTicket(ticket, date, time);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/qr/{ticketId}")
    ApiResponse<String> renderQr(@PathVariable(name = "ticketId") String ticket) throws Exception {
        return ApiResponse.<String>builder()
                .result(ticketService.renderQr(ticket))
                .build();
    }

    @PostMapping("/checkin/first")
    ApiResponse<Void> firstCheckin(@RequestHeader(name = "station") String station,
                                   @Valid @RequestBody FirstCheckinRequest request) throws JsonProcessingException {
        ticketService.checkinFirstStep(station, request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/checkin/second")
    ApiResponse<Void> secondCheckin(@RequestHeader(name = "station") String station,
                                    @Valid @RequestBody SecondCheckinRequest request) throws JsonProcessingException {
        ticketService.checkinSecondStep(station, request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/checkout/first")
    ApiResponse<Void> firstCheckout(@RequestHeader(name = "station") String station,
                                    @Valid @RequestBody FirstCheckoutRequest request) throws JsonProcessingException {
        ticketService.checkoutFirstStep(station, request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/checkout/second")
    ApiResponse<Void> secondCheckout(@RequestHeader(name = "station") String station,
                                     @Valid @RequestBody SecondCheckoutRequest request) throws JsonProcessingException {
        ticketService.checkoutSecondStep(station, request);
        return ApiResponse.<Void>builder()
                .build();
    }

}
