package com.parking.vault_service.repository;

import com.parking.vault_service.entity.Deposit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositRepository extends MongoRepository<Deposit, String> {
    Page<Deposit> findAllByActionAtIsNullAndCancelAtIsNull(Pageable pageable);

    Page<Deposit> findAllByActionAtIsNotNull(Pageable pageable);

    Page<Deposit> findAllByCancelAtIsNotNull(Pageable pageable);

    Optional<Deposit> findByIdAndOwnerId(String id, String owner);

    Page<Deposit> findAllByOwnerIdAndActionAtIsNotNull(String ownerId, Pageable pageable);

    Page<Deposit> findAllByOwnerIdAndActionAtIsNull(String ownerId, Pageable pageable);

    List<Deposit> findByIdInAndActionAtIsNull(List<String> idDeposits);

    int countByOwnerIdAndCancelAtIsNullAndActionAtIsNull(String owner);

    Page<Deposit> findAllByOwnerIdAndCancelAtIsNotNull(String owner, Pageable pageable);

    List<Deposit> findByIdInAndActionAtIsNullAndCancelAtIsNull(List<String> depositsId);

}
