package com.parking.vault_service.mapper;

import com.parking.vault_service.dto.request.OwnerCreationRequest;
import com.parking.vault_service.dto.response.OwnerResponse;
import com.parking.vault_service.entity.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    @Mapping(source = "uid", target = "id")
    Owner toOwner(OwnerCreationRequest request);


    OwnerResponse toOwnerCreationResponse(Owner owner);
}

