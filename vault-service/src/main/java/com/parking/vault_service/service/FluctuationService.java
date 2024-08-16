package com.parking.vault_service.service;

import com.parking.vault_service.mapper.FluctuationMapper;
import com.parking.vault_service.repository.FluctuationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FluctuationService {

    FluctuationRepository fluctuationRepository;
    FluctuationMapper fluctuationMapper;
}
