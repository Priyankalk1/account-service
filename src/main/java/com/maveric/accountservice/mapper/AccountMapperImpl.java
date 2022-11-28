package com.maveric.accountservice.mapper;

import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.model.Account;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class AccountMapperImpl implements AccountMapper{
    @Override
    public Account map(AccountDto accountDto) {
        return Account.builder()
                ._id(accountDto.get_id())
                .customerId(accountDto.getCustomerId())
                .type(accountDto.getType())
                .createdAt(accountDto.getCreatedAt())
                .updatedAt(accountDto.getUpdatedAt())
                .build();
    }

    @Override
    public AccountDto map(Account account) {
        return AccountDto.builder()
                ._id(account.get_id())
                .customerId(account.getCustomerId())
                .type(account.getType())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    @Override
    public List<Account> mapToModel(List<AccountDto> accountsDto) {
        return accountsDto.stream().map(accountDto ->  Account.builder()
                ._id(accountDto.get_id())
                .customerId(accountDto.getCustomerId())
                .type(accountDto.getType())
                .createdAt(accountDto.getCreatedAt())
                .updatedAt(accountDto.getUpdatedAt())
                .build()
        ).toList();
    }

    @Override
    public List<AccountDto> mapToDto(List<Account> accounts) {
        return accounts.stream().map(account ->  AccountDto.builder()
                ._id(account.get_id())
                .customerId(account.getCustomerId())
                .type(account.getType())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build()
        ).toList();
    }
}
