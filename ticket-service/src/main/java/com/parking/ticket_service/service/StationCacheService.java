package com.parking.ticket_service.service;

import com.parking.ticket_service.entity.Station;
import com.parking.ticket_service.exception.AppException;
import com.parking.ticket_service.exception.ErrorCode;
import com.parking.ticket_service.repository.StationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StationCacheService {

    StationRepository stationRepository;

    @Cacheable(value = "station", key = "#stationId", unless = "#result == null")
    public Station getStation(String stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
    }
}
