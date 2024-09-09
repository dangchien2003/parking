package com.parking.vault_service.controller;

import com.parking.vault_service.dto.request.AddFluctuationRequest;
import com.parking.vault_service.dto.response.ApiResponse;
import com.parking.vault_service.service.FluctuationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/fluctuation")
public class InternalFluctuationController {
    FluctuationService fluctuationService;

    @PostMapping("/add/{reason}")
    ApiResponse<Void> ticketPurchase(@Valid @RequestBody AddFluctuationRequest request,
                                     @PathVariable(name = "reason") String reason) {
        fluctuationService.addFluctuation(request, reason);
        return ApiResponse.<Void>builder()
                .build();
    }
}
