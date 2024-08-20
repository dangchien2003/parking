package com.parking.ticket_service.mapper;

import com.parking.ticket_service.entity.Category;
import com.parking.ticket_service.entity.CategoryHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryHistoryMapper {

    @Mapping(source = "id", target = "category.id")
    @Mapping(target = "id", ignore = true)
    CategoryHistory toCategoryHistory(Category category);
}
