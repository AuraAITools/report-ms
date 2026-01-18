package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.links.AccountEducatorLink;
import com.aura.core.entities.links.AccountStudentLink;
import com.aura.core.repositories.AccountRepository;
import com.aura.details.springdata.mappers.accounts.AccountMapper;
import com.aura.details.springdata.mappers.links.AccountEducatorLinkMapper;
import com.aura.details.springdata.mappers.links.AccountStudentLinkMapper;
import com.aura.details.springdata.repositories.jpa.accounts.JpaAccountRepository;
import com.aura.details.springdata.repositories.jpa.educators.JpaEducatorRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountStudentLinkRepository;
import com.aura.details.springdata.repositories.jpa.students.JpaStudentRepository;
import com.aura.details.springdata.tables.accounts.AccountTable;
import com.aura.details.springdata.tables.educators.EducatorTable;
import com.aura.details.springdata.tables.links.AccountEducatorLinkTable;
import com.aura.details.springdata.tables.links.AccountStudentLinkTable;
import com.aura.details.springdata.tables.students.StudentTable;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public class AccountRepositoryImpl implements AccountRepository {
    private final JpaAccountRepository jpaAccountRepository;

    private final JpaAccountStudentLinkRepository jpaAccountStudentLinkRepository;

    private final JpaAccountEducatorLinkRepository jpaAccountEducatorLinkRepository;

    private final JpaStudentRepository jpaStudentRepository;

    private final JpaEducatorRepository jpaEducatorRepository;

    public AccountRepositoryImpl(JpaAccountRepository jpaAccountRepository, JpaAccountStudentLinkRepository jpaAccountStudentLinkRepository, JpaAccountEducatorLinkRepository jpaAccountEducatorLinkRepository, JpaStudentRepository jpaStudentRepository, JpaEducatorRepository jpaEducatorRepository) {
        this.jpaAccountRepository = jpaAccountRepository;
        this.jpaAccountStudentLinkRepository = jpaAccountStudentLinkRepository;
        this.jpaAccountEducatorLinkRepository = jpaAccountEducatorLinkRepository;
        this.jpaStudentRepository = jpaStudentRepository;
        this.jpaEducatorRepository = jpaEducatorRepository;
    }

    @Override
    public Account save(Account account) {
        AccountTable accountTable = AccountMapper.INSTANCE.toTableEntity(account);
        AccountTable savedAccountTable = jpaAccountRepository.save(accountTable);
        return AccountMapper.INSTANCE.toDomainEntity(savedAccountTable);
    }

    @Override
    public List<AccountStudentLink> linkAccountToStudents(String accountId, List<String> studentIds) {
        AccountTable accountTable = jpaAccountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new RuntimeException("accountId not found"));
        List<AccountStudentLinkTable> accountStudentLinkTables = studentIds.stream().map(sid -> {
            StudentTable studentTable = jpaStudentRepository.getReferenceById(UUID.fromString(sid));
            AccountStudentLinkTable accountStudentLinkTable = AccountStudentLinkTable.builder()
                    .account(accountTable)
                    .student(studentTable)
                    .tenantId(accountTable.getTenantId())
                    .build();
            return accountStudentLinkTable;
        }).toList();
        return jpaAccountStudentLinkRepository.saveAll(accountStudentLinkTables).stream().map(AccountStudentLinkMapper.INSTANCE::toDomain).toList();
    }

    @Override
    public AccountStudentLink linkAccountToStudent(String accountId, String studentId) {
        AccountTable accountTable = jpaAccountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new RuntimeException("accountId not found"));
        StudentTable studentTable = jpaStudentRepository.getReferenceById(UUID.fromString(studentId));
        AccountStudentLinkTable accountStudentLinkTable = AccountStudentLinkTable.builder()
                .account(accountTable)
                .student(studentTable)
                .tenantId(accountTable.getTenantId())
                .build();
        return AccountStudentLinkMapper.INSTANCE.toDomain(jpaAccountStudentLinkRepository.save(accountStudentLinkTable));
    }

    @Override
    public AccountEducatorLink linkAccountToEducator(String accountId, String educatorId) {
        AccountTable accountTable = jpaAccountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new RuntimeException("account not found"));
        EducatorTable educatorTable = jpaEducatorRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new RuntimeException("educator not found"));
        AccountEducatorLinkTable accountEducatorLinkTable = AccountEducatorLinkTable
                .builder()
                .account(accountTable)
                .educator(educatorTable)
                .tenantId(accountTable.getTenantId())
                .build();
        return AccountEducatorLinkMapper.INSTANCE.toDomain(jpaAccountEducatorLinkRepository.save(accountEducatorLinkTable));
    }

    @Override
    public @Nullable Account findById(String id) {
        return jpaAccountRepository.findById(UUID.fromString(id)).map(AccountMapper.INSTANCE::toDomainEntity).orElse(null);
    }
}
