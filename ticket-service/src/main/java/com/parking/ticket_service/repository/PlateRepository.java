package com.parking.ticket_service.repository;

import com.parking.ticket_service.entity.Plate;
import com.parking.ticket_service.entity.PlateId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlateRepository extends JpaRepository<Plate, PlateId> {
}
