package com.maveric.accountservice.mapper;

import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.model.Account;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.maveric.accountservice.AccountServiceApplicationTests.getAccount;
import static com.maveric.accountservice.AccountServiceApplicationTests.getAccountDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountMapperImplTest {

    private AccountMapperImpl accountMapper = new AccountMapperImpl();

    @Test
    void map() {
        Account account = accountMapper.map(getAccountDto());
        assertEquals(getAccount().get_id(),account.get_id());
    }

    @Test
    void testMap() {
        AccountDto accountDto = accountMapper.map(getAccount());
        assertEquals(getAccountDto().get_id(),accountDto.get_id());
    }

    @Test
    void mapToModel() {
        List<Account> account = accountMapper.mapToModel(Arrays.asList(getAccountDto(),getAccountDto()));
        assertEquals(2,account.size());
    }

    @Test
    void mapToModel_failure() {
        List<Account> account = accountMapper.mapToModel(Arrays.asList());
        assertEquals(0,account.size());
    }

    @Test
    void mapToDto() {
        List<AccountDto> accountDto = accountMapper.mapToDto(Arrays.asList(getAccount(),getAccount()));
        assertEquals(2,accountDto.size());
    }

    @Test
    void mapToDto_failure() {
        List<AccountDto> accountDto = accountMapper.mapToDto(Arrays.asList());
        assertEquals(0,accountDto.size());
    }
}