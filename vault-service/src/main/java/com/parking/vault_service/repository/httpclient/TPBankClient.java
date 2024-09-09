package com.parking.vault_service.repository.httpclient;

import com.parking.vault_service.dto.request.TPBankAuthentication;
import com.parking.vault_service.dto.response.TPBankAuthenticationResponse;
import com.parking.vault_service.dto.response.TPBankHistoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "TPBank", url = "https://ebank.tpb.vn/gateway/api")
public interface TPBankClient {
    @PostMapping(value = "/auth/login/v3", consumes = "application/json")
    TPBankAuthenticationResponse authen(@RequestHeader("DEVICE_ID") String deviceId,
                                        @RequestHeader("PLATFORM_NAME") String platformName,
                                        @RequestBody TPBankAuthentication request);

    @PostMapping(value = "/smart-search-presentation-service/v2/account-transactions/find", consumes = "application/json")
    TPBankHistoryResponse history(@RequestHeader("Authorization") String token,
                                  @RequestBody Object request);
}
