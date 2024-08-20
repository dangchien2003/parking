package com.parking.ticket_service.repository;

import com.parking.ticket_service.entity.Ticket;
import com.parking.ticket_service.entity.TicketId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, TicketId> {
}
