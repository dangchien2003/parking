package com.parking.ticket_service.service;

import com.parking.ticket_service.entity.Plate;
import com.parking.ticket_service.entity.PlateId;
import com.parking.ticket_service.repository.PlateRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlateCacheService {

    PlateRepository plateRepository;

    @Cacheable(value = "plate", key = "#ticketId + '-' + #turn", unless = "#result == null")
    public Plate getPlate(String ticketId, int turn) {
        PlateId plateId = new PlateId(ticketId, turn);
        return plateRepository.findById(plateId)
                .orElse(null);
    }

    @CacheEvict(value = "plate", key = "#ticketId + '-' + #turn")
    public void deletePlate(String ticketId, int turn) {
    }
}
