package com.parking.ticket_service.mapper;

import com.parking.ticket_service.dto.response.TicketResponse;
import com.parking.ticket_service.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketResponse toTicketResponse(Ticket ticket);

}
