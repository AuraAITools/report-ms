package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.dtos.responses.AccountDTO;
import com.reportai.www.reportapi.entities.IAccount;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.ParentRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.reportai.www.reportapi.dtos.responses.AccountDTO.ACCOUNT_TYPES.EDUCATOR;
import static com.reportai.www.reportapi.dtos.responses.AccountDTO.ACCOUNT_TYPES.INSTITUTION;
import static com.reportai.www.reportapi.dtos.responses.AccountDTO.ACCOUNT_TYPES.PARENT;
import static com.reportai.www.reportapi.dtos.responses.AccountDTO.ACCOUNT_TYPES.STUDENT;

@Service
public class AccountService {
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final InstitutionRepository institutionRepository;
    private final EducatorRepository educatorRepository;

    public AccountService(ParentRepository parentRepository, StudentRepository studentRepository, InstitutionRepository institutionRepository, EducatorRepository educatorRepository) {
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.institutionRepository = institutionRepository;
        this.educatorRepository = educatorRepository;
    }

    public List<AccountDTO> getAllAccountsOfUser(UUID userId) {
        List<AccountDTO> userAccounts = new ArrayList<>();
        parentRepository.findByUserId(userId).ifPresent(acc -> userAccounts.add(AccountDTO.from(acc, PARENT)));
        studentRepository.findByUserId(userId).ifPresent(acc -> userAccounts.add(AccountDTO.from(acc, STUDENT)));
        educatorRepository.findByUserId(userId).ifPresent(acc -> userAccounts.add(AccountDTO.from(acc, EDUCATOR)));
        institutionRepository.findByUserId(userId).ifPresent(acc -> userAccounts.add(AccountDTO.from(acc, INSTITUTION)));
        return userAccounts;
    }
}
