package com.parking.vault_service.service;

import com.parking.vault_service.dto.request.TPBankAuthentication;
import com.parking.vault_service.dto.request.TPBankHistoryBodyRequest;
import com.parking.vault_service.dto.response.TPBankAuthenticationResponse;
import com.parking.vault_service.dto.response.TPBankHistoryResponse;
import com.parking.vault_service.dto.response.TransactionInfo;
import com.parking.vault_service.entity.Deposit;
import com.parking.vault_service.entity.Fluctuation;
import com.parking.vault_service.entity.Owner;
import com.parking.vault_service.enums.EPersonal;
import com.parking.vault_service.enums.EReason;
import com.parking.vault_service.enums.ETransaction;
import com.parking.vault_service.mapper.FluctuationMapper;
import com.parking.vault_service.repository.DepositRepository;
import com.parking.vault_service.repository.FluctuationRepository;
import com.parking.vault_service.repository.OwnerRepository;
import com.parking.vault_service.repository.httpclient.TPBankClient;
import com.parking.vault_service.utils.PageUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApproveDeposit {

    DepositRepository depositRepository;
    OwnerRepository ownerRepository;
    FluctuationRepository fluctuationRepository;
    TPBankClient tpbankClient;
    FluctuationMapper fluctuationMapper;
    TPBankAuthentication tpbankAuthentication;
    TPBankHistoryBodyRequest tpbankHistoryBodyRequest;

    @NonFinal
    static final String DEVICE_ID = "kpVAPvlf34EVbSUJmzPpjURgxxiX1D7CtVbCS8Pt35SQ0";

    @NonFinal
    static final String PLATFORM = "WEB";

    @NonFinal
    String accessToken;

    @NonFinal
    long reauthAfter;

    @NonFinal
    Instant lastAuth;

    public List<TransactionInfo> getHistory(String date) {
        setAccessToken();
        setTimeHistory(date);
        TPBankHistoryResponse history = tpbankClient.history(accessToken, tpbankHistoryBodyRequest);
        return history.getTransactionInfos();
    }

    public void autoApprove() {
        List<String> depositsApprove = getDepositApprove();
        if (depositsApprove.isEmpty())
            return;

        List<Deposit> deposits = depositRepository.findAllById(depositsApprove);
        approveAction(deposits, "TPBank-System auto approves deposit request");
    }

    List<String> getDepositApprove() {
        Pageable pageable = PageUtil.getPageable(1, 800, "asc", "id");
        List<Deposit> depositsWaiting = depositRepository
                .findAllByActionAtIsNullAndCancelAtIsNull(pageable)
                .getContent();

        if (depositsWaiting.isEmpty()) {
            return new ArrayList<>();
        }

        List<TransactionInfo> history = getHistory(null);
        history.sort(Comparator.comparing(TransactionInfo::getDescription));

        int depositIndex = 0;
        List<String> depositsApprove = new ArrayList<>();
        boolean stopLoop = false;
        for (TransactionInfo order : history) {
            while (true) {
                if (depositIndex > depositsWaiting.size() - 1) {
                    stopLoop = true;
                    break;
                }

                Deposit deposit = depositsWaiting.get(depositIndex);
                int compare = order.getDescription().compareTo(
                        deposit.getId());

                if (compare > 0) {
                    depositIndex++;
                    continue;
                } else if (compare == 0 && deposit.getAmount() == order.getAmount()) {
                    depositsApprove.add(deposit.getId());
                    depositIndex++;
                }
                break;
            }

            if (stopLoop)
                break;
        }
        return depositsApprove;
    }

    // format date yyyy-MM-dd
    void setTimeHistory(String date) {
        final String formatDate = "yyyyMMdd";
        String fromDate;
        LocalDate endTime;
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        if (date == null || date.isEmpty()) {
            endTime = LocalDate.now();
            fromDate = endTime.atStartOfDay(zoneId)
                    .minusDays(30)
                    .format(DateTimeFormatter.ofPattern(formatDate));
        } else {
            endTime = LocalDate.parse(date);
            fromDate = endTime.atStartOfDay(zoneId)
                    .format(DateTimeFormatter.ofPattern(formatDate));
        }

        String toDate = endTime.atTime(LocalTime.MAX)
                .atZone(zoneId)
                .format(DateTimeFormatter.ofPattern(formatDate));

        tpbankHistoryBodyRequest.setFromDate(fromDate);
        tpbankHistoryBodyRequest.setToDate(toDate);
    }

    void setAccessToken() {
        Instant now = Instant.now();
        if (Objects.isNull(lastAuth) || ChronoUnit.SECONDS.between(lastAuth, now) > reauthAfter) {
            TPBankAuthenticationResponse auth = tpbankClient.authen(DEVICE_ID, PLATFORM, tpbankAuthentication);
            accessToken = "Bearer " + auth.getAccess_token();
            reauthAfter = auth.getExpires_in() - 120;//seconds
            lastAuth = now;
        }
    }

    List<Fluctuation> approveAction(List<Deposit> deposits, String note) {

        List<Fluctuation> fluctuations = getNewFluctuation(deposits, note);

        List<Owner> owners = getOwnersUpdateBalance(deposits);

        depositRepository.saveAll(deposits);
        ownerRepository.saveAll(owners);
        return fluctuationRepository.saveAll(fluctuations);
    }

    List<Owner> getOwnersUpdateBalance(List<Deposit> deposits) {

        List<String> listOwnerId = new ArrayList<>();
        Map<String, Integer> data = new HashMap<>();

        deposits.stream()
                .collect(Collectors.groupingBy(
                        Deposit::getOwnerId,
                        Collectors.summingInt(Deposit::getAmount)
                ))
                .forEach((key, value) -> {
                    listOwnerId.add(key);
                    data.put(key, value);
                });

        List<Owner> owners = ownerRepository.findAllById(listOwnerId);

        owners.forEach(owner -> {
            int totalAmountPlus = data.get(owner.getId());
            owner.setBalance(owner.getBalance() + totalAmountPlus);
        });

        return owners;
    }

    List<Fluctuation> getNewFluctuation(List<Deposit> deposits, String note) {

        List<Fluctuation> fluctuations = new ArrayList<>();

        long now = Instant.now().toEpochMilli();
        for (Deposit deposit : deposits) {
            Fluctuation fluctuation = fluctuationMapper.toFluctuation(deposit);
            fluctuation.setReason(EReason.APPROVE.getValue());
            fluctuation.setTransaction(ETransaction.CREDIT.getValue());
            fluctuation.setDescription(note);
            fluctuation.setCreateAt(now);

            fluctuations.add(fluctuation);

            deposit.setActionAt(now);
            deposit.setActionBy(EPersonal.STAFF.name());
        }

        return fluctuations;
    }


}
