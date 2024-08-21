package com.parking.vault_service.repository;

import com.parking.vault_service.entity.Fluctuation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FluctuationRepository extends MongoRepository<Fluctuation, String> {
    Page<Fluctuation> findAllByReason(String reason, Pageable pageable);

    Page<Fluctuation> findAllByReasonAndOwnerId(String reason, String owner, Pageable pageable);

    Page<Fluctuation> findAllByReasonNotIn(List<String> reasons, Pageable pageable);

    Page<Fluctuation> findAllByReasonNotInAndOwnerId(List<String> reasons, String owner, Pageable pageable);
}
