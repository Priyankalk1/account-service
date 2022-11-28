package com.maveric.accountservice.service;

import com.maveric.accountservice.dto.AccountDto;

import java.util.List;

public interface AccountService {

    public AccountDto createAccount(String customerId,AccountDto transaction);
    public List<AccountDto> getAccounts(Integer page, Integer pageSize);
    public List<AccountDto> getAccountByUserId(Integer page, Integer pageSize,String userId);
    public AccountDto getAccountDetailsById(String accountId);
    public AccountDto updateAccountDetails(String customerId,String accountId,AccountDto accountDto);
    public String deleteAccount(String accountId);
}
