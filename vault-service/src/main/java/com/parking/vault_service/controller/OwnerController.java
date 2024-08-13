package com.parking.vault_service.controller;

import com.parking.vault_service.dto.response.ApiResponse;
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
@RequestMapping("/owner")
public class OwnerController {

    OwnerService ownerService;

    @GetMapping("/{uid}")
    ApiResponse<OwnerResponse> getOwner(@PathVariable(name = "uid") String uid) {
        return ApiResponse.<OwnerResponse>builder()
                .result(ownerService.getOwner(uid))
                .build();
    }

}
