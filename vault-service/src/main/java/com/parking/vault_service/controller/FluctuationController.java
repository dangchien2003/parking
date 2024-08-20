package com.parking.vault_service.controller;

import com.parking.vault_service.dto.request.AddFluctuationRequest;
import com.parking.vault_service.dto.response.AddFuctuationResponse;
import com.parking.vault_service.dto.response.ApiResponse;
import com.parking.vault_service.service.FluctuationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/fluctuation")
public class FluctuationController {
    FluctuationService fluctuationService;

    @PostMapping("/buy-ticket")
    ApiResponse<AddFuctuationResponse> ticketPurchase(@Valid @RequestBody AddFluctuationRequest request) {
        return ApiResponse.<AddFuctuationResponse>builder()
                .result(fluctuationService.ticketPurchase(request))
                .build();
    }

    @PostMapping("/cancel-ticket")
    ApiResponse<Void> ticketCancel(@Valid @RequestBody AddFluctuationRequest request) {
        fluctuationService.ticketCancel(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/all/{type}")
    ApiResponse<Object> getAccepted(
            @PathVariable(name = "type")
            String type,

            @RequestParam(name = "page")
            @Min(value = 1)
            int page,

            @RequestParam(name = "sort", required = false)
            String sort
    ) {
        return null;
    }
//
//    nạp tiền thành công của user1 sắp xếp theo giảm dần của ngày tao, trang 1
//    nạp thu hồi tiền của user1 sắp xếp theo giảm dần của ngày tao, trang 1
//    mua vé của user1 sắp xếp theo giảm dần của ngày tao, trang 1
//    vé tăng hạn của user1 sắp xếp theo giảm dần của ngày tao, trang 1
//    phaan loại khác
}
