package com.aura.core.usecases.accounts;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.educators.Educator;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.EducatorRepository;
import lombok.RequiredArgsConstructor;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class CreateEducatorInAccountUseCaseImpl implements CreateEducatorInAccountUseCase {
    private final AccountRepository accountRepository;
    private final EducatorRepository educatorRepository;

    @Override
    public Educator createEducator(String accountId, Educator educator) {
        Account account = ofNullable(accountRepository.findById(accountId)).orElseThrow(() -> new RuntimeException("account does not exists"));
        Educator createdEducator = educatorRepository.save(educator);
        accountRepository.linkAccountToEducator(accountId, createdEducator.getId());
        return createdEducator;
    }
}
