package com.parking.identity_service.repository.httpclient;

import com.parking.identity_service.configuration.AuthenticationRequestInterceptor;
import com.parking.identity_service.dto.request.CustomerProfileCreationRequest;
import com.parking.identity_service.dto.response.ApiResponse;
import com.parking.identity_service.dto.response.CustomerProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${feign.client.config.profile-service.url}", configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @PostMapping(value = "/internal/customer", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<CustomerProfileResponse> customerCreateProfile(@RequestBody CustomerProfileCreationRequest request);
}
