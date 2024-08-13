package com.parking.identity_service.repository.httpclient;

import com.parking.identity_service.configuration.AuthenticationRequestInterceptor;
import com.parking.identity_service.dto.request.OwnerCreationRequest;
import com.parking.identity_service.dto.response.OwnerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "vault-service", url = "${feign.client.config.vault-service.url}", configuration = {AuthenticationRequestInterceptor.class})
public interface VaultClient {
    @PostMapping(value = "/internal/owner/registration", produces = MediaType.APPLICATION_JSON_VALUE)
    OwnerResponse createOwner(@RequestBody OwnerCreationRequest request);
}
