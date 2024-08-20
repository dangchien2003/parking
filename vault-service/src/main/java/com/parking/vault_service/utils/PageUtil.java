package com.parking.vault_service.utils;

import com.parking.vault_service.dto.response.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PageUtil {

    private PageUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static <T> PageResponse<T> renderPageResponse(List<T> data, int currentPage, int pageSize) {
        return PageResponse.<T>builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .data(data)
                .build();
    }

    public static Pageable getPageable(int page, int pageAmount, Sort sort) {
        --page;

        return !Objects.isNull(sort)
                ? PageRequest.of(page, pageAmount, sort)
                : PageRequest.of(page, pageAmount, getSort("desc", "createAt"));
    }

    public static Pageable getPageable(int page, int pageAmount, String sort, String field) {
        --page;

        return PageRequest.of(page, pageAmount, getSort(sort, field));
    }

    public static Sort getSort(String type, String field) {

        try {
            type = type.toUpperCase(Locale.ROOT);
        } catch (Exception e) {
            type = "DESC";
        }

        if (type.equals("ASC")) {
            return Sort.by(Sort.Order.asc(field));
        } else {
            return Sort.by(Sort.Order.desc(field));
        }
    }
}
