package com.parking.ticket_service.repository.httpclient;

import com.parking.ticket_service.configuration.AuthenticationRequestInterceptor;
import com.parking.ticket_service.dto.request.FlucTicketPurchaseRequest;
import com.parking.ticket_service.dto.response.AddFuctuationResponse;
import com.parking.ticket_service.dto.response.ApiResponse;
import com.parking.ticket_service.dto.response.BalenceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "vault-client", url = "${client.vault-service}", configuration = {AuthenticationRequestInterceptor.class})
public interface VaultClient {
    @GetMapping(value = "/owners/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<BalenceResponse> getBalance();

    @PostMapping(value = "/fluctuation", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<AddFuctuationResponse> ticketPurchase(@RequestBody FlucTicketPurchaseRequest request);
}
