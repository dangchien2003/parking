package com.parking.vault_service.controller;

import com.parking.vault_service.dto.request.AdminUpdateOwnerRequest;
import com.parking.vault_service.dto.request.OwnerUpdateRequest;
import com.parking.vault_service.dto.response.ApiResponse;
import com.parking.vault_service.entity.Wallet;
import com.parking.vault_service.service.WalletService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/wallet")
public class WalletController {

    WalletService walletStatusService;

    @PatchMapping("/update/{type}")
    ApiResponse<List<Wallet>> update(@PathVariable(name = "type") String type,
                                     @Valid @RequestBody AdminUpdateOwnerRequest request) {
        return ApiResponse.<List<Wallet>>builder()
                .result(walletStatusService.update(type, request))
                .build();
    }

    @PatchMapping("/me/update/{type}")
    ApiResponse<Void> update(@PathVariable(name = "type") String type,
                             @Valid @RequestBody OwnerUpdateRequest request) {
        walletStatusService.update(type, request);
        return ApiResponse.<Void>builder()
                .build();
    }
}
