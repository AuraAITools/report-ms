package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.links.AccountEducatorLink;
import com.aura.core.entities.links.AccountStudentLink;
import com.aura.core.repositories.AccountRepository;
import com.aura.details.springdata.mappers.accounts.AccountMapper;
import com.aura.details.springdata.mappers.links.AccountEducatorLinkMapper;
import com.aura.details.springdata.mappers.links.AccountStudentLinkMapper;
import com.aura.details.springdata.models.accounts.AccountModel;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.links.AccountEducatorLinkModel;
import com.aura.details.springdata.models.links.AccountStudentLinkModel;
import com.aura.details.springdata.models.students.StudentModel;
import com.aura.details.springdata.repositories.jpa.accounts.JpaAccountRepository;
import com.aura.details.springdata.repositories.jpa.educators.JpaEducatorRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountStudentLinkRepository;
import com.aura.details.springdata.repositories.jpa.students.JpaStudentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
    private final JpaAccountRepository jpaAccountRepository;
    private final JpaAccountStudentLinkRepository jpaAccountStudentLinkRepository;
    private final JpaAccountEducatorLinkRepository jpaAccountEducatorLinkRepository;
    private final JpaStudentRepository jpaStudentRepository;
    private final JpaEducatorRepository jpaEducatorRepository;

    @Override
    public Account save(Account account) {
        AccountModel accountModel = AccountMapper.INSTANCE.toModelEntity(account);
        AccountModel savedAccountModel = jpaAccountRepository.save(accountModel);
        return AccountMapper.INSTANCE.toDomainEntity(savedAccountModel);
    }

    @Override
    public List<AccountStudentLink> linkAccountToStudents(String accountId, List<String> studentIds) {
        AccountModel accountModel = jpaAccountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new RuntimeException("accountId not found"));
        List<AccountStudentLinkModel> accountStudentLinkModels = studentIds.stream().map(sid -> {
            StudentModel studentModel = jpaStudentRepository.getReferenceById(UUID.fromString(sid));
            AccountStudentLinkModel accountStudentLinkModel = AccountStudentLinkModel.builder()
                    .account(accountModel)
                    .student(studentModel)
                    .tenantId(accountModel.getTenantId())
                    .build();
            return accountStudentLinkModel;
        }).toList();
        return jpaAccountStudentLinkRepository.saveAll(accountStudentLinkModels).stream().map(AccountStudentLinkMapper.INSTANCE::toDomain).toList();
    }

    @Override
    public AccountStudentLink linkAccountToStudent(String accountId, String studentId) {
        AccountModel accountModel = jpaAccountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new RuntimeException("accountId not found"));
        StudentModel studentModel = jpaStudentRepository.getReferenceById(UUID.fromString(studentId));
        AccountStudentLinkModel accountStudentLinkModel = AccountStudentLinkModel.builder()
                .account(accountModel)
                .student(studentModel)
                .tenantId(accountModel.getTenantId())
                .build();
        return AccountStudentLinkMapper.INSTANCE.toDomain(jpaAccountStudentLinkRepository.save(accountStudentLinkModel));
    }

    @Override
    public AccountEducatorLink linkAccountToEducator(String accountId, String educatorId) {
        AccountModel accountModel = jpaAccountRepository.findById(UUID.fromString(accountId)).orElseThrow(() -> new RuntimeException("account not found"));
        EducatorModel educatorModel = jpaEducatorRepository.findById(UUID.fromString(educatorId)).orElseThrow(() -> new RuntimeException("educator not found"));
        AccountEducatorLinkModel accountEducatorLinkModel = AccountEducatorLinkModel
                .builder()
                .account(accountModel)
                .educator(educatorModel)
                .tenantId(accountModel.getTenantId())
                .build();
        return AccountEducatorLinkMapper.INSTANCE.toDomain(jpaAccountEducatorLinkRepository.save(accountEducatorLinkModel));
    }

    @Override
    public @Nullable Account findById(String id) {
        return jpaAccountRepository.findById(UUID.fromString(id)).map(AccountMapper.INSTANCE::toDomainEntity).orElse(null);
    }
}
