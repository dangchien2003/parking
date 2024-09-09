package com.parking.ticket_service.service;

import com.parking.ticket_service.entity.Ticket;
import com.parking.ticket_service.repository.TicketRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class TicketCacheService {
    TicketRepository ticketRepository;

    @Cacheable(value = "ticket", key = "#ticketId", unless = "#result == null")
    public Ticket getTicket(String ticketId, String uid) {
        return ticketRepository.findByIdAndUid(ticketId, uid)
                .orElse(null);
    }

    @CacheEvict(value = "ticket", key = "#ticketId")
    public void deleteTicket(String ticketId) {
    }

    @Cacheable(value = "checking", key = "#ticketId+'-checking'", unless = "#result == false")
    public boolean isTicketChecking(String ticketId) {
        return false;
    }

    @CachePut(value = "checking", key = "#ticketId+'-checking'")
    public boolean addTicketChecking(String ticketId) {
        return true;
    }
}
