package com.parking.ticket_service.controller;

import com.parking.ticket_service.dto.request.StationCreationRequest;
import com.parking.ticket_service.dto.request.StationUpdateRequest;
import com.parking.ticket_service.dto.response.ApiResponse;
import com.parking.ticket_service.dto.response.PageResponse;
import com.parking.ticket_service.dto.response.StationResponse;
import com.parking.ticket_service.service.StationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/station")
@PreAuthorize("hasAnyAuthority('ROLE_STAFF')")
public class StationController {

    StationService stationService;

    @PostMapping
    ApiResponse<StationResponse> create(@Valid @RequestBody StationCreationRequest request) {
        return ApiResponse.<StationResponse>builder()
                .result(stationService.create(request))
                .build();
    }

    @PatchMapping
    ApiResponse<StationResponse> update(@Valid @RequestBody StationUpdateRequest request) {
        return ApiResponse.<StationResponse>builder()
                .result(stationService.update(request))
                .build();
    }

    @GetMapping("/all/{type}")
    ApiResponse<PageResponse<StationResponse>> getAll(
            @PathVariable(name = "type")
            String type,

            @RequestParam(name = "page", required = false, defaultValue = "1")
            @Min(value = 1)
            int page,

            @RequestParam(name = "sort", required = false, defaultValue = "desc")
            String sort,

            @RequestParam(name = "field", required = false, defaultValue = "CreateAt")
            String field
    ) {
        return ApiResponse.<PageResponse<StationResponse>>builder()
                .result(stationService.findAll(page, type, sort, field))
                .build();
    }

    @PutMapping("/{station}/{type}")
    ApiResponse<StationResponse> updateStatus(
            @PathVariable(name = "station")
            String station,

            @PathVariable(name = "type")
            String type) {
        return ApiResponse.<StationResponse>builder()
                .result(stationService.updateStatus(station, type))
                .build();
    }

    @DeleteMapping("/{station}")
    ApiResponse<Void> updateStatus(
            @PathVariable(name = "station")
            String station) {
        stationService.delete(station);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/address")
    ApiResponse<List<String>> searchAddress(@RequestParam("key") String query,
                                            @RequestParam("quantity") int quantity) {
        return ApiResponse.<List<String>>builder()
                .result(stationService.searchAddress(query, quantity))
                .build();
    }
}
