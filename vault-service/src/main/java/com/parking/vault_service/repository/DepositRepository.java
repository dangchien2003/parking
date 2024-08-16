package com.parking.vault_service.repository;

import com.parking.vault_service.entity.Deposit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends MongoRepository<Deposit, String> {
    List<Deposit> findAllByActionAt(long actionAt, Pageable pageable);

    List<Deposit> findAllByActionAtGreaterThan(long actionAt, Pageable pageable);

    List<Deposit> findAllByOwnerId(String ownerId, Pageable pageable);

    List<Deposit> findAllByOwnerIdAndActionAtGreaterThan(String ownerId, long actionAt, Pageable pageable);

    List<Deposit> findAllByOwnerIdAndActionAt(String ownerId, long actionAt, Pageable pageable);

    List<Deposit> findAllByIdInAndActionAtIsNull(List<String> idDeposits);

}
