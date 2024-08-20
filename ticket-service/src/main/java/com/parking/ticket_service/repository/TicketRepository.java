package com.parking.ticket_service.repository;

import com.parking.ticket_service.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    Optional<Ticket> findByIdAndUid(String id, String uid);
}
