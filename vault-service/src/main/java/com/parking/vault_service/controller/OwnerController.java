package com.parking.vault_service.controller;

import com.parking.vault_service.dto.response.ApiResponse;
import com.parking.vault_service.dto.response.BalanceResponse;
import com.parking.vault_service.dto.response.OwnerResponse;
import com.parking.vault_service.service.OwnerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/owners")
public class OwnerController {

    OwnerService ownerService;

    @GetMapping("/{uid}")
    ApiResponse<OwnerResponse> getInfo(@PathVariable(name = "uid") String uid) {
        return ApiResponse.<OwnerResponse>builder()
                .result(ownerService.getInfo(uid))
                .build();
    }

    @GetMapping("/balance")
    ApiResponse<BalanceResponse> getBalance() {
        return ApiResponse.<BalanceResponse>builder()
                .result(ownerService.getBalance())
                .build();
    }
}
