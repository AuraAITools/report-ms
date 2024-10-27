package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
