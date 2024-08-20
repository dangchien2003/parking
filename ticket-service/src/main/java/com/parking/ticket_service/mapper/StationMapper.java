package com.parking.ticket_service.mapper;

import com.parking.ticket_service.dto.request.StationCreationRequest;
import com.parking.ticket_service.dto.request.StationUpdateRequest;
import com.parking.ticket_service.dto.response.StationResponse;
import com.parking.ticket_service.entity.Station;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StationMapper {

    Station toStation(StationCreationRequest request);

    void toStation(StationUpdateRequest request, @MappingTarget Station station);

    StationResponse toStationResponse(Station station);


}
