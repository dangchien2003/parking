package com.parking.ticket_service.repository;

import com.parking.ticket_service.entity.Category;
import com.parking.ticket_service.entity.CategoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryHistoryRepository extends JpaRepository<CategoryHistory, Integer> {
    List<CategoryHistory> findAllByCategoryOrderByCreateAtDesc(Category category);
}
