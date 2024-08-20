package com.parking.ticket_service.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Locale;
import java.util.Objects;

public class PageUtils {

    private PageUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Pageable getPageable(int page, int pageAmount, Sort sort) {
        --page;

        return !Objects.isNull(sort)
                ? PageRequest.of(page, pageAmount, sort)
                : PageRequest.of(page, pageAmount, Sort.by(Sort.Order.desc("createAt")));
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
