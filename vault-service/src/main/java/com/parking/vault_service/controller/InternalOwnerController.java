package com.parking.vault_service.controller;

import com.parking.vault_service.dto.request.OwnerCreationRequest;
import com.parking.vault_service.dto.response.ApiResponse;
import com.parking.vault_service.dto.response.OwnerResponse;
import com.parking.vault_service.service.OwnerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/owner")
public class InternalOwnerController {

    OwnerService ownerService;

    @PostMapping("/registration")
    ApiResponse<OwnerResponse> create(@Valid @RequestBody OwnerCreationRequest request) {
        return ApiResponse.<OwnerResponse>builder()
                .result(ownerService.create(request))
                .build();
    }
}
