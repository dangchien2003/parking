package com.parking.vault_service.repository;

import com.parking.vault_service.entity.Owner;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends MongoRepository<Owner, String> {
}
