package com.parking.ticket_service.repository;

import com.parking.ticket_service.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    Page<Category> findAllByStatus(String status, Pageable pageable);

}
