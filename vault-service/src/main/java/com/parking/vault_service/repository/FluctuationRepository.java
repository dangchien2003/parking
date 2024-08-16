package com.parking.vault_service.repository;

import com.parking.vault_service.entity.Fluctuation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FluctuationRepository extends MongoRepository<Fluctuation, String> {
}
