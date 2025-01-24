package com.reportai.www.reportapi.repositories;

import com.reportai.www.reportapi.entities.Account;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {
    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUserId(String userId);

//    @Query("SELECT a FROM Account a JOIN a.institutions i WHERE a.email = :email AND i.id = :institutionId")
//    Optional<Account> findByEmailAndInstitutionId(@Param("email") String email, @Param("institutionId") String institutionId);
//

}
