package com.parking.vault_service.repository;

import com.parking.vault_service.entity.Deposit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends MongoRepository<Deposit, String> {
    Page<Deposit> findAllByActionAtIsNull(Pageable pageable);

    Page<Deposit> findAllByActionAtIsNotNull(Pageable pageable);

    List<Deposit> findAllByOwnerId(String ownerId, Pageable pageable);

    Page<Deposit> findAllByOwnerIdAndActionAtIsNotNull(String ownerId, Pageable pageable);

    Page<Deposit> findAllByOwnerIdAndActionAtIsNull(String ownerId, Pageable pageable);

    List<Deposit> findAllByIdInAndActionAtIsNull(List<String> idDeposits);

}
