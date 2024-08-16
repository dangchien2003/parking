package com.parking.vault_service.mapper;

import com.parking.vault_service.dto.request.DepositCreationRequest;
import com.parking.vault_service.dto.response.DepositResponse;
import com.parking.vault_service.entity.Deposit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepositMapper {

    @Mapping(source = "code", target = "id")
    Deposit toDeposit(DepositCreationRequest request);

    DepositResponse toDepositResponse(Deposit cash);
}
