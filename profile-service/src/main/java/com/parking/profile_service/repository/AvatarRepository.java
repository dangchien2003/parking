package com.parking.profile_service.repository;

import com.parking.profile_service.entity.Avatar;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends Neo4jRepository<Avatar, String> {
}
