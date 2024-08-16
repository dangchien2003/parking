package com.parking.profile_service.repository;

import com.parking.profile_service.entity.ProfileCustomer;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerProfileRepository extends Neo4jRepository<ProfileCustomer, String> {
    int countByPhone(String phone);
}
