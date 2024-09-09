package com.parking.ticket_service.service;

import com.parking.ticket_service.dto.request.StationCreationRequest;
import com.parking.ticket_service.dto.request.StationUpdateRequest;
import com.parking.ticket_service.dto.response.DataAddress;
import com.parking.ticket_service.dto.response.PageResponse;
import com.parking.ticket_service.dto.response.StationResponse;
import com.parking.ticket_service.entity.Station;
import com.parking.ticket_service.enums.AmountPage;
import com.parking.ticket_service.enums.EFilterTypeStation;
import com.parking.ticket_service.enums.EStationStatus;
import com.parking.ticket_service.enums.ProvinceIdVN;
import com.parking.ticket_service.exception.AppException;
import com.parking.ticket_service.exception.ErrorCode;
import com.parking.ticket_service.mapper.StationMapper;
import com.parking.ticket_service.repository.StationRepository;
import com.parking.ticket_service.repository.httpclient.AddressClient;
import com.parking.ticket_service.utils.ENumUtils;
import com.parking.ticket_service.utils.FieldCheckers;
import com.parking.ticket_service.utils.PageUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class StationService {

    StationRepository stationRepository;
    StationMapper stationMapper;
    AddressClient addressClient;

    public void delete(String stationId) {
        stationRepository.deleteById(stationId);
    }

    public StationResponse updateStatus(String stationId, String type) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new AppException(ErrorCode.STATION_NOT_EXIST));

        EStationStatus currentStatus = ENumUtils.getType(EStationStatus.class, station.getStatus());
        EStationStatus statusUpdate = ENumUtils.getType(EStationStatus.class, type);
        if (currentStatus.equals(statusUpdate)) {
            throw new AppException(ErrorCode.UPDATE_FAIL);
        }

        station.setStatus(statusUpdate.name());
        station.setModifiedAt(Instant.now().toEpochMilli());

        station = stationRepository.save(station);
        return stationMapper.toStationResponse(station);
    }

    public StationResponse update(StationUpdateRequest request) {
        Station station = stationRepository.findById(request.getStationId())
                .orElseThrow(() -> new AppException(ErrorCode.STATION_NOT_EXIST));

        if (station.getProvince().equals(request.getProvince())
                && station.getDistrict().equals(request.getDistrict())
                && station.getCommune().equals(request.getCommune())
                && station.getRoad().equals(request.getRoad()))
            throw new AppException(ErrorCode.UPDATE_FAIL);

        stationMapper.toStation(request, station);
        station.setModifiedAt(Instant.now().toEpochMilli());

        station = stationRepository.save(station);
        return stationMapper.toStationResponse(station);
    }

    public StationResponse create(StationCreationRequest request) {

        if (!isAddress(request.getProvince(), request.getDistrict(), request.getCommune()))
            throw new AppException(ErrorCode.INVALID_ADDRESS);

        Station station = stationRepository
                .findByProvinceAndDistrictAndCommuneAndRoad(
                        request.getProvince(),
                        request.getDistrict(),
                        request.getCommune(),
                        request.getRoad())
                .orElse(null);

        if (!Objects.isNull(station))
            throw new AppException(ErrorCode.ADDRESS_EXISTED);

        long now = Instant.now().toEpochMilli();
        station = stationMapper.toStation(request);
        station.setStatus(EStationStatus.PENDING.name());
        station.setCreateAt(now);
        station.setModifiedAt(now);

        station = stationRepository.save(station);
        return stationMapper.toStationResponse(station);
    }

    public PageResponse<StationResponse> findAll(int page, String type, String sort, String field) {
        if (!FieldCheckers.hasField(Station.class, field))
            field = "createAt";

        EFilterTypeStation filterType;
        try {
            filterType = ENumUtils.getType(EFilterTypeStation.class, type);
        } catch (AppException e) {
            throw new AppException(ErrorCode.NOTFOUND_URL);
        }

        Pageable pageable = PageUtils.
                getPageable(page, AmountPage.FIND_STATION.getAmount(), PageUtils.getSort(sort, field));

        Page<Station> pageData;
        switch (filterType) {
            case ANY -> pageData = stationRepository.findAll(pageable);
            case ACTIVE -> pageData = stationRepository
                    .findAllByStatus(EStationStatus.ACTIVE.name(), pageable);
            case PENDING -> pageData = stationRepository
                    .findAllByStatus(EStationStatus.PENDING.name(), pageable);
            case INACTIVE -> pageData = stationRepository
                    .findAllByStatus(EStationStatus.INACTIVE.name(), pageable);
            default -> throw new AppException(ErrorCode.NOTFOUND_FILTER);
        }

        return PageResponse.<StationResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .data(pageData.stream()
                        .map(stationMapper::toStationResponse)
                        .toList())
                .build();
    }

    public List<String> searchAddress(String query, int quantity) {

        List<Station> candidates = stationRepository.findAllByStatus(EStationStatus.ACTIVE.name());

        LevenshteinDistance levenshtein = new LevenshteinDistance();
        return candidates.stream()
                .sorted((a, b) -> Integer.compare(
                        levenshtein.apply(query, getAddressFromStation(a)),
                        levenshtein.apply(query, getAddressFromStation(b)))
                )
                .limit(quantity)
                .map(this::getAddressFromStation)
                .toList();
    }

    String getAddressFromStation(Station station) {
        StringJoiner stringJoiner = new StringJoiner(", ");

        stringJoiner.add(station.getRoad());
        stringJoiner.add(station.getCommune());
        stringJoiner.add(station.getDistrict());
        stringJoiner.add(station.getProvince());

        return stringJoiner.toString();
    }

    boolean isAddress(String province, String district, String commune) {
        String id = getProvinceId(convertAddress(province));
        if (id == null) return false;

        id = getIdFromAddressList(addressClient.getDistrict(id).getData(), district);
        if (id == null) return false;

        id = getIdFromAddressList(addressClient.getCommune(id).getData(), commune);
        return id != null;
    }

    String getProvinceId(String province) {
        try {
            return ProvinceIdVN.valueOf(province).getId();
        } catch (Exception e) {
            return null;
        }
    }

    String getIdFromAddressList(List<DataAddress> data, String address) {
        for (DataAddress dataAddress : data) {
            if (dataAddress.getName().equals(address)) {
                return dataAddress.getId();
            }
        }
        return null;
    }

    String convertAddress(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[Đđ]", "d")
                .replaceAll("[\\s-]+", "_")
                .replaceAll("_+", "_")
                .toUpperCase();
    }


}
