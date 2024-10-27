package com.reportai.www.reportapi.services;

import com.reportai.www.reportapi.entities.Account;

public interface IAccountService {
    Account createAccount(Account account);

    boolean deleteAccount(String email);
}
