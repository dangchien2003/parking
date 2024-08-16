package com.parking.vault_service.controller;

import com.parking.vault_service.dto.request.DepositApproveRequest;
import com.parking.vault_service.dto.request.DepositCreationRequest;
import com.parking.vault_service.dto.response.ApiResponse;
import com.parking.vault_service.dto.response.DepositResponse;
import com.parking.vault_service.entity.Deposit;
import com.parking.vault_service.entity.Fluctuation;
import com.parking.vault_service.service.DepositService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/deposit")
public class DepositController {

    DepositService depositService;

    @PostMapping
    ApiResponse<DepositResponse> create(@Valid @RequestBody DepositCreationRequest request) {
        return ApiResponse.<DepositResponse>builder()
                .result(depositService.create(request))
                .build();
    }

    @GetMapping("/all/{type}")
    ApiResponse<List<Deposit>> getAll(
            @PathVariable(name = "type")
            String type,
            @RequestParam(name = "page")
            @Min(value = 1)
            int page,
            @RequestParam(name = "sort", required = false)
            String sort
    ) {
        return ApiResponse.<List<Deposit>>builder()
                .result(depositService.getAll(type, page, sort))
                .build();
    }

    @GetMapping("/{type}")
    ApiResponse<Object> getAccepted(
            @PathVariable(name = "type")
            String type,
            @RequestParam(name = "page")
            @Min(value = 1)
            int page,
            @RequestParam(name = "sort", required = false)
            String sort
    ) {
        return ApiResponse.<Object>builder()
                .result(depositService.customerGetAll(type, page, sort))
                .build();
    }

    @GetMapping("/cancelled")
    ApiResponse<Object> getCancelled(Object request) {
        return ApiResponse.<Object>builder()
                .result(null)
                .build();
    }

    @PatchMapping("/cancel/{id}")
    ApiResponse<Object> cancel(Object request) {
        return ApiResponse.<Object>builder()
                .result(null)
                .build();
    }

    @GetMapping("/statistics/deposit")
    ApiResponse<Object> deposit(Object request) {
        return ApiResponse.<Object>builder()
                .result(null)
                .build();
    }


    @PostMapping("approve")
    ApiResponse<List<Fluctuation>> approve(@Valid @RequestBody DepositApproveRequest request) {
        return ApiResponse.<List<Fluctuation>>builder()
                .result(depositService.approveDeposit(request))
                .build();
    }
}
