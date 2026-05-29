package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.AccountFeature;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.links.AccountEducatorLink;
import com.aura.core.entities.links.AccountStudentLink;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.AccountRepository;
import com.aura.details.springdata.mappers.accounts.AccountMapper;
import com.aura.details.springdata.mappers.educators.EducatorMapper;
import com.aura.details.springdata.mappers.links.AccountEducatorLinkMapper;
import com.aura.details.springdata.mappers.links.AccountStudentLinkMapper;
import com.aura.details.springdata.mappers.students.StudentMapper;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public Account saveAndFlush(Account account) {
        AccountModel accountModel = AccountMapper.INSTANCE.toModelEntity(account);
        AccountModel savedAccountModel = jpaAccountRepository.saveAndFlush(accountModel);
        return AccountMapper.INSTANCE.toDomainEntity(savedAccountModel);
    }

    @Override
    public void updateStateById(String id, Account.STATE state) {
        jpaAccountRepository.updateStateById(UUID.fromString(id), AccountModel.STATE.valueOf(state.name()));
    }

    @Override
    public void enableFeature(String id, AccountFeature feature) {
        UUID uuid = UUID.fromString(id);
        switch (feature) {
            case PARENT_FEATURE -> jpaAccountRepository.enableParentFeatureById(uuid);
            case EDUCATOR_FEATURE -> jpaAccountRepository.enableEducatorFeatureById(uuid);
            case STAFF_FEATURE -> jpaAccountRepository.enableStaffFeatureById(uuid);
        }
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

    @Override
    public @Nullable Account findByEmailAndTenantId(String email, String tenantId) {
        return Optional.ofNullable(jpaAccountRepository.findByEmailAndTenantId(email, tenantId)).map(AccountMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public List<Account> findAllByEmail(String email) {
        return jpaAccountRepository.findAllByEmail(email).stream().map(AccountMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public @Nullable Account findByUserIdAndTenantId(String userId, String tenantId) {
        return Optional.ofNullable(jpaAccountRepository.findByUserIdAndTenantId(userId, tenantId)).map(AccountMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public List<Account> findAllByTenantId(String tenantId) {
        return jpaAccountRepository.findByTenantId(tenantId).stream()
                .map(AccountMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public Map<String, List<Student>> findStudentsByAccountIds(List<String> accountIds) {
        if (accountIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = accountIds.stream().map(UUID::fromString).toList();
        Map<String, List<Student>> result = new HashMap<>();
        for (Object[] row : jpaAccountStudentLinkRepository.findStudentsByAccountIds(uuids)) {
            String accountId = row[0].toString();
            StudentModel studentModel = (StudentModel) row[1];
            result.computeIfAbsent(accountId, k -> new ArrayList<>())
                    .add(StudentMapper.INSTANCE.toDomainEntity(studentModel));
        }
        return result;
    }

    @Override
    public Map<String, List<Educator>> findEducatorsByAccountIds(List<String> accountIds) {
        if (accountIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = accountIds.stream().map(UUID::fromString).toList();
        Map<String, List<Educator>> result = new HashMap<>();
        for (Object[] row : jpaAccountEducatorLinkRepository.findEducatorsByAccountIds(uuids)) {
            String accountId = row[0].toString();
            EducatorModel educatorModel = (EducatorModel) row[1];
            result.computeIfAbsent(accountId, k -> new ArrayList<>())
                    .add(EducatorMapper.INSTANCE.toDomainEntity(educatorModel));
        }
        return result;
    }

    @Override
    public Map<String, Account> findAllByEducatorIds(List<String> educatorIds) {
        if (educatorIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = educatorIds.stream().map(UUID::fromString).toList();
        Map<String, Account> result = new HashMap<>();
        for (Object[] row : jpaAccountEducatorLinkRepository.findAccountsByEducatorIds(uuids)) {
            String educatorId = row[0].toString();
            AccountModel accountModel = (AccountModel) row[1];
            result.put(educatorId, AccountMapper.INSTANCE.toDomainEntity(accountModel));
        }
        return result;
    }
}
