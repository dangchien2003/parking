package com.parking.ticket_service.mapper;

import com.parking.ticket_service.dto.response.ContentQr;
import com.parking.ticket_service.dto.response.InfoTicketResponse;
import com.parking.ticket_service.dto.response.TicketResponse;
import com.parking.ticket_service.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    @Mapping(source = "id", target = "ticketId")
    TicketResponse toTicketResponse(Ticket ticket);

    InfoTicketResponse toInfoTicketResponse(Ticket ticket);

    @Mapping(source = "category.unit", target = "unit")
    @Mapping(source = "id", target = "ticket")
    ContentQr toContentQr(Ticket ticket);
}
