package com.parking.ticket_service.controller;

import com.parking.ticket_service.dto.request.CategoryCreatitonRequest;
import com.parking.ticket_service.dto.request.CategoryUpdateRequest;
import com.parking.ticket_service.dto.request.CategoryUpdateStationRequest;
import com.parking.ticket_service.dto.response.ApiResponse;
import com.parking.ticket_service.dto.response.CategoryResponse;
import com.parking.ticket_service.dto.response.PageResponse;
import com.parking.ticket_service.entity.Category;
import com.parking.ticket_service.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/category")
public class CategoryController {

    CategoryService categoryService;

    @PostMapping
    ApiResponse<CategoryResponse> create(@Valid @RequestBody CategoryCreatitonRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.create(request))
                .build();
    }

    @PatchMapping
    ApiResponse<CategoryResponse> update(@Valid @RequestBody CategoryUpdateRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.update(request))
                .build();
    }

    @GetMapping("/all/{type}")
    ApiResponse<PageResponse<Category>> findAll(
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
        return ApiResponse.<PageResponse<Category>>builder()
                .result(categoryService.findAll(type, page, sort, field))
                .build();
    }

    @PutMapping("/update/station")
    ApiResponse<Category> update(@Valid @RequestBody CategoryUpdateStationRequest request) {
        return ApiResponse.<Category>builder()
                .result(categoryService.update(request))
                .build();
    }
}
