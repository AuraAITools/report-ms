package com.reportai.www.reportapi.services;

public interface IAccountService<T> {
    T createAccount(T account);

    T createDefaultAccount(T account);
}
