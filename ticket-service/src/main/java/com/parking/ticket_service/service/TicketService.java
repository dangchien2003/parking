package com.parking.ticket_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.ticket_service.dto.request.*;
import com.parking.ticket_service.dto.response.*;
import com.parking.ticket_service.entity.*;
import com.parking.ticket_service.enums.*;
import com.parking.ticket_service.exception.AppException;
import com.parking.ticket_service.exception.ErrorCode;
import com.parking.ticket_service.mapper.TicketMapper;
import com.parking.ticket_service.repository.*;
import com.parking.ticket_service.repository.httpclient.VaultClient;
import com.parking.ticket_service.repository.uploader.CloudinaryUploader;
import com.parking.ticket_service.utils.AESUtils;
import com.parking.ticket_service.utils.ENumUtils;
import com.parking.ticket_service.utils.TimeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class TicketService {

    TicketRepository ticketRepository;
    CategoryRepository categoryRepository;
    CategoryHistoryRepository categoryHistoryRepository;
    RedisRepository redisRepository;
    PlateRepository plateRepository;
    PlateCacheService plateCacheService;
    TicketCacheService ticketCacheService;
    StationCacheService stationCacheService;
    TicketMapper ticketMapper;
    VaultClient vaultClient;
    ObjectMapper objectMapper;
    CloudinaryUploader cloudinaryUploader;

    static final long EXTENDED_UNIT_PRICE_ONE_MINUTE = 1_000;
    static final String KEY_CANCEL_QR = "CANCELED_";

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public void cancelQr(CancelQrRequest request) throws JsonProcessingException {
        ContentQr contentQr = getContentQr(request.getQr());
        validateOwner(contentQr);

        long seconds = (contentQr.getExpireAt() - Instant.now().toEpochMilli()) / 1000;

        if (seconds <= 0)
            return;

        redisRepository.saveValue(KEY_CANCEL_QR + contentQr.getId(), true, Duration.of(seconds, ChronoUnit.SECONDS));
    }

    void validateOwner(ContentQr contentQr) {
        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        if (!contentQr.getUid().equals(uid))
            throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    public void checkoutFirstStep(String stationId, FirstCheckoutRequest request) throws JsonProcessingException {
        ContentQr contentQr = getContentQr(request.getQr());
        validateQrId(contentQr.getId());

        Station station = stationCacheService.getStation(stationId);
        validateStationStatus(station);

        Ticket ticket = getTicket(contentQr.getTicket());
        validateTicketValidity(ticket);

        Plate plate = plateCacheService.getPlate(ticket.getId(), ticket.getTurnTotal());

        validateTicketInUse(ticket, plate);
        validateStationManagingTicket(plate, station);

        ticketCacheService.addTicketChecking(ticket.getId());
    }

    void validateQrId(String qrid) {
        if (!Objects.isNull(redisRepository.getValue(KEY_CANCEL_QR + qrid)))
            throw new AppException(ErrorCode.TICKET_NOTFOUND);
    }

    void validateTicketInUse(Ticket ticket, Plate plate) {
        if (ticket.getTurnTotal() <= 0 ||
                Objects.isNull(plate) ||
                plate.getCheckoutAt() > 0)
            throw new AppException(ErrorCode.UNUSED_TICKET);
    }

    void validateStationManagingTicket(Plate plate, Station station) {
        if (!plate.getStation().getStationId().equals(station.getStationId()))
            throw new AppException(ErrorCode.DIFFERENT_STATION);
    }

    public void checkoutSecondStep(String stationId, SecondCheckoutRequest request) throws JsonProcessingException {
        ContentQr contentQr = getContentQr(request.getQr());
        validateTicketChecking(contentQr);

        Ticket ticket = ticketCacheService.getTicket(contentQr.getTicket(), contentQr.getUid());

        Station station = getStationInSecondStep(stationId);

        Plate plate = plateCacheService.getPlate(ticket.getId(), ticket.getTurnTotal());

        validateStationManagingTicket(plate, station);
        validatePlate(plate, request.getImage());

        String path = uploadPlate(ticket.getId(), ticket.getTurnTotal(), request.getImage(), "checkout");

        plate.setCheckoutAt(Instant.now().toEpochMilli());
        plate.setImageOut(path);
        plateRepository.save(plate);
        plateCacheService.deletePlate(ticket.getId(), ticket.getTurnTotal());
    }

    void validatePlate(Plate plate, String image) {

        if (Objects.isNull(plate) ||
                plate.getCheckoutAt() > 0)
            throw new AppException(ErrorCode.CHECKIN_NOT_YET);

        String scanPlate = fakeScanPlate(image);

        if (!plate.getContentPlate().equals(scanPlate))
            throw new AppException(ErrorCode.INCORRECT_PLATE);
    }

    void validateTicketChecking(ContentQr contentQr) {
        if (!ticketCacheService.isTicketChecking(contentQr.getTicket()))
            throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    Station getStationInSecondStep(String stationId) {
        try {
            return stationCacheService.getStation(stationId);
        } catch (AppException e) {
            throw new AppException(ErrorCode.STATION_NOT_EXIST);
        }
    }


    public void checkinFirstStep(String stationId, FirstCheckinRequest request) throws JsonProcessingException {
        ContentQr contentQr = getContentQr(request.getQr());
        validateQrId(contentQr.getId());

        Station station = stationCacheService.getStation(stationId);
        validateStationStatus(station);

        Ticket ticket = getTicket(contentQr.getTicket());

        validateTicketValidity(ticket);

        validateTicketBeforeCheckin(ticket);

        validateStationAndCategory(ticket, station);

        ticketCacheService.addTicketChecking(ticket.getId());
    }

    void validateTicketValidity(Ticket ticket) {
        if (ticket.getCancleAt() > 0 || ticket.getExpireAt() < Instant.now().toEpochMilli()) {
            throw new AppException(ErrorCode.TICKET_NO_LONGER_VALID);
        }
    }

    void validateStationStatus(Station station) {
        if (!station.getStatus().equals(EStationStatus.ACTIVE.name())) {
            throw new AppException(ErrorCode.STATION_NOT_SUPPORT);
        }
    }

    void validateTicketBeforeCheckin(Ticket ticket) {
        if (ticket.getTurnTotal() > 0) {
            Plate plate = plateCacheService.getPlate(ticket.getId(), ticket.getTurnTotal());
            if (!Objects.isNull(plate) && plate.getCheckoutAt() == 0 && plate.getCheckinAt() > 0) {
                throw new AppException(ErrorCode.TICKET_IN_USE);
            }
        }
    }

    void validateStationAndCategory(Ticket ticket, Station station) {
        if (ticket.getCategory().getUnit().equals(ECategoryUnit.TIMES.name()) &&
                ticket.getTurnTotal() + 1 > ticket.getCategory().getQuantity()) {
            throw new AppException(ErrorCode.TICKET_NO_LONGER_VALID);
        }

        Category category = categoryRepository.findById(ticket.getCategory().getCategory().getId())
                .orElseThrow(() -> new AppException(ErrorCode.ERROR_TICKET));

        if (category.getStations().stream()
                .noneMatch(element -> element.getStationId().equals(station.getStationId())))
            throw new AppException(ErrorCode.STATION_NOT_SUPPORT_TICKET);
    }

    public void checkinSecondStep(String stationId, SecondCheckinRequest request) throws JsonProcessingException {
        ContentQr contentQr = getContentQr(request.getQr());
        validateTicketChecking(contentQr);

        Ticket ticket = ticketCacheService.getTicket(contentQr.getTicket(), contentQr.getUid());
        Station station = getStationInSecondStep(stationId);

        String scanPlate = fakeScanPlate(request.getImage());

        validatePlate(ticket, scanPlate);

        String path = uploadPlate(ticket.getId(), ticket.getTurnTotal() + 1, request.getImage(), "checkin");

        PlateId plateId = new PlateId(ticket.getId(), ticket.getTurnTotal() + 1);
        Plate newPlate = Plate.builder()
                .id(plateId)
                .urlPrefixCode(UrlPrefixCodeImage.V1.getCode())
                .imageIn(path)
                .checkinAt(Instant.now().toEpochMilli())
                .contentPlate(scanPlate)
                .station(station)
                .build();

        ticket.setTurnTotal(ticket.getTurnTotal() + 1);
        if (ticket.getCategory().getUnit().equals(ECategoryUnit.TIMES.name()))
            ticket.setExpireAt(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli());

        plateRepository.save(newPlate);
        ticketRepository.save(ticket);
        ticketCacheService.deleteTicket(ticket.getId());
    }

    String fakeScanPlate(String image) {
        return "123";
    }

    void validatePlate(Ticket ticket, String scanPlate) {
        if (!Objects.isNull(ticket.getContentPlate()) &&
                !ticket.getContentPlate().equals(scanPlate))
            throw new AppException(ErrorCode.INVALID_PLATE);
    }

    String uploadPlate(String ticket, int turn, String image, String suffix) {
        String nameImage = ticket + "_" + turn + "_" + suffix;
        String folder = ECloudinary.FOLDER_PLATE.getValue();

        try {
            cloudinaryUploader.asyncUploadBase64Image(image, "parking/" + folder, nameImage);
        } catch (IOException e) {
            log.error("cloudinaryUploader error: ", e);
        }

        return folder + '/' + nameImage;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public String renderQr(String ticketId) throws Exception {

        Ticket ticket = getTicket(ticketId);

        if (ticket.getCancleAt() > 0 ||
                ticket.getExpireAt() < Instant.now().toEpochMilli())
            throw new AppException(ErrorCode.TICKET_NO_LONGER_VALID);

        Plate plate = null;
        if (ticket.getCategory().getUnit().equals(ECategoryUnit.TIMES.name()) &&
                ticket.getTurnTotal() >= ticket.getCategory().getQuantity()) {
            plate = plateCacheService.getPlate(ticket.getId(), ticket.getTurnTotal());

            if (Objects.isNull(plate) || plate.getCheckoutAt() > 0)
                throw new AppException(ErrorCode.TICKET_NO_LONGER_VALID);
        }

        ContentQr contentQr = ticketMapper.toContentQr(ticket);
        contentQr.setId(UUID.randomUUID().toString());

        byte[] compressedData = objectMapper.writeValueAsString(contentQr).getBytes();
        return AESUtils.encrypt(compressedData);
    }

    ContentQr getContentQr(String encrypt) throws JsonProcessingException {
        String decompressedData;
        try {
            byte[] decryptedCompressedData = AESUtils.decrypt(encrypt);
            decompressedData = new String(decryptedCompressedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new AppException(ErrorCode.TICKET_NOTFOUND);
        }

        return objectMapper.readValue(decompressedData, ContentQr.class);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public InfoTicketResponse getInfoTicket(String ticketId) {
        return ticketMapper.toInfoTicketResponse(
                getTicket(ticketId));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public void extendTicket(String ticketId, String date, String time) {

        Ticket ticket = getTicket(ticketId);

        Plate plate = plateCacheService.getPlate(ticket.getId(), ticket.getTurnTotal());
        if (Objects.isNull(plate) || plate.getCheckoutAt() > 0)
            throw new AppException(ErrorCode.UNUSED_TICKET);

        if (ticket.getCancleAt() > 0 ||
                ticket.getExpireAt() > Instant.now().toEpochMilli() ||
                ticket.getTurnTotal() == 0)
            throw new AppException(ErrorCode.EXTEND_FAIL);


        String expireStr = date + " " + time;
        String formatExpire = "yyyy-MM-dd HH:mm";
        if (!TimeUtils.isValidDateTime(expireStr, formatExpire))
            throw new AppException(ErrorCode.INVALID_FORMAT_DATETIME);

        long newExpire = TimeUtils.timeToLong(expireStr, formatExpire);
        long difference = Duration
                .between(Instant.now(), Instant.ofEpochMilli(newExpire))
                .toMinutes();

        if (difference < 15) {
            throw new AppException(ErrorCode.INVALID_NEW_EXPIRE);
        }

        long amount = difference * EXTENDED_UNIT_PRICE_ONE_MINUTE;
        BalenceResponse balance = vaultClient.getBalance().getResult();

        if (balance.getBalence() < amount)
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);


        AddFluctuationRequest addFluctuationRequest = AddFluctuationRequest.builder()
                .amount((int) amount)
                .objectId(ticket.getId())
                .build();

        vaultClient.addFluctuation(addFluctuationRequest, EReason.EXTEND_TICKET.name());

        // gửi thông báo

        ticket.setExpireAt(newExpire);
        ticketRepository.save(ticket);
    }

    Ticket getTicket(String ticketId) {
        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return ticketCacheService.getTicket(ticketId, uid);
    }

    Ticket getTicket(String ticketId, String uid) {
        return Objects.isNull(uid)
                ? ticketRepository.findById(ticketId)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED))
                : ticketRepository.findByIdAndUid(ticketId, uid)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public void cancel(String ticketId) {

        Ticket ticket = getTicket(ticketId);

        if (ticket.getTurnTotal() > 0 ||
                ticket.getCancleAt() > 0 ||
                !Objects.isNull(ticket.getContentPlate()))
            throw new AppException(ErrorCode.CANNOT_CANCEL_TICKET);


        AddFluctuationRequest addFluctuationRequest = AddFluctuationRequest.builder()
                .amount(ticket.getCategory().getPrice())
                .objectId(ticketId)
                .build();
        vaultClient.addFluctuation(addFluctuationRequest, EReason.CANCEL_TICKET.name());

        ticket.setCancleAt(Instant.now().toEpochMilli());
        ticketRepository.save(ticket);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public TicketResponse updatePlate(TicketUpdatePlateRequest request) {

        Ticket ticket = getTicket(request.getTicketId());

        if ((ticket.getTurnTotal() > 0 && !Objects.isNull(ticket.getContentPlate())) ||
                ticket.getCancleAt() > 0
        )
            throw new AppException(ErrorCode.CANNOT_UPDATE_PLATE);

        ticket.setContentPlate(request.getPlate());

        return ticketMapper.toTicketResponse(ticketRepository.save(ticket));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER')")
    public TicketResponse buy(BuyTicketRequest request) {

        String uid = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Category category = categoryRepository.findById(request.getCategory())
                .orElse(null);

        if (Objects.isNull(category) || !category.getStatus().equals(ECategoryStatus.ACTIVE.name()))
            throw new AppException(ErrorCode.INVALID_CATEGORY);
        long expire;
        CategoryHistory history;
        try {
            history = categoryHistoryRepository.findAllByCategoryOrderByCreateAtDesc(category)
                    .getFirst();

            if (!history.getStatus().equals(ECategoryStatus.ACTIVE.name()))
                throw new AppException(ErrorCode.DATA_NOT_FOUND);

            expire = getExpireTicket(history.getUnit());
        } catch (Exception e) {
            log.error("Buy fail: ", e);
            throw new AppException(ErrorCode.CANNOT_BUY_TICKET);
        }

        ApiResponse<BalenceResponse> balanceResponse = vaultClient.getBalance();

        if (balanceResponse.getResult().getBalence() < history.getPrice())
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);

        String ticketId = UUID.randomUUID().toString();

        AddFluctuationRequest addFluctuationRequest = AddFluctuationRequest.builder()
                .objectId(ticketId)
                .amount(history.getPrice())
                .build();
        vaultClient.addFluctuation(addFluctuationRequest, EReason.BUY_TICKET.name());

        Ticket ticket = Ticket.builder()
                .id(ticketId)
                .uid(uid)
                .category(history)
                .buyAt(Instant.now().toEpochMilli())
                .expireAt(expire)
                .build();

        ticket = ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(ticket);
    }

    long getExpireTicket(String unit) {

        ECategoryUnit typeUnit;
        try {
            typeUnit = ENumUtils.getType(ECategoryUnit.class, unit);
        } catch (AppException e) {
            throw new AppException(ErrorCode.CANNOT_BUY_TICKET);
        }

        ChronoUnit chronoUnit;
        switch (typeUnit) {
            case DAY, TIMES -> chronoUnit = ChronoUnit.DAYS;
            case WEEK -> chronoUnit = ChronoUnit.WEEKS;
            case MONTH -> chronoUnit = ChronoUnit.MONTHS;
            default -> throw new AppException(ErrorCode.NOTFOUND_CATEGORY_UNIT);
        }

        return Instant.now().plus(1, chronoUnit).toEpochMilli();
    }

}
