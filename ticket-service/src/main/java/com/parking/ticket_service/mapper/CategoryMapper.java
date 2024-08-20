package com.parking.ticket_service.mapper;

import com.parking.ticket_service.dto.request.CategoryCreatitonRequest;
import com.parking.ticket_service.dto.request.CategoryUpdateRequest;
import com.parking.ticket_service.dto.response.CategoryResponse;
import com.parking.ticket_service.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryCreatitonRequest request);

    void toCategory(CategoryUpdateRequest request, @MappingTarget Category category);

    CategoryResponse toCategoryResponse(Category category);
}
