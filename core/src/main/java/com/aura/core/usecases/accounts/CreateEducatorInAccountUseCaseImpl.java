package com.aura.core.usecases.accounts;

import com.aura.core.clients.IdentityService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.educators.Educator;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.EducatorRepository;


import static java.util.Optional.ofNullable;

public class CreateEducatorInAccountUseCaseImpl implements CreateEducatorInAccountUseCase {
    private final AccountRepository accountRepository;
    private final EducatorRepository educatorRepository;
    private final IdentityService identityService;

    public CreateEducatorInAccountUseCaseImpl(AccountRepository accountRepository, EducatorRepository educatorRepository, IdentityService identityService) {
        this.accountRepository = accountRepository;
        this.educatorRepository = educatorRepository;
        this.identityService = identityService;
    }

    @Override
    public Account createEducator(String accountId, Educator educator) {
        Account account = ofNullable(accountRepository.findById(accountId)).orElseThrow(() -> new RuntimeException("account does not exists"));
        Educator createdEducator = educatorRepository.save(educator);
        accountRepository.linkAccountToEducator(accountId, educator.getId()); // throws unique constraint exception if already linked

        var assignEducatorRoleUseCase = new AssignEducatorRoleUseCaseImpl(identityService, createdEducator.getTenantId(), account.getUserId());
        assignEducatorRoleUseCase.assignRole();
        // TODO: send email to educator
        return account;
    }
}
