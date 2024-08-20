package com.parking.ticket_service.repository;

import com.parking.ticket_service.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, String> {

    Optional<Station> findByProvinceAndDistrictAndCommuneAndRoad(String province,
                                                                 String district,
                                                                 String commune,
                                                                 String road);

    Page<Station> findAllByStatus(String status, Pageable pageable);

    List<Station> findAllByStatus(String status);

}
