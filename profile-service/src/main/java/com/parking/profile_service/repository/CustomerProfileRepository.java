package com.parking.profile_service.repository;

import com.parking.profile_service.entity.CustomerProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerProfileRepository extends Neo4jRepository<CustomerProfile, String> {
}
