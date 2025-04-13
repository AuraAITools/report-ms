package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Account;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {
    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);

    List<Account> findAllByEmail(String email);

    Optional<Account> findByUserId(String userId);

    @Override
    @Transactional
    default <S extends Account> S save(S entity) {
        try {
            System.out.println("Attempting to save account: " + entity);
            S savedEntity = saveAndFlush(entity);
            System.out.println("Account saved successfully: " + savedEntity);
            return savedEntity;
        } catch (Exception e) {
            System.err.println("Error saving account: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
