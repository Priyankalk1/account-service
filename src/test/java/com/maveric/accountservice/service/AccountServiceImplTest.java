package com.maveric.accountservice.service;

import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.enums.AccountType;
import com.maveric.accountservice.exception.AccountNotFoundException;
import com.maveric.accountservice.exception.PathParamsVsInputParamsMismatchException;
import com.maveric.accountservice.mapper.AccountMapperImpl;
import com.maveric.accountservice.model.Account;
import com.maveric.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.maveric.accountservice.AccountServiceApplicationTests.getAccount;
import static com.maveric.accountservice.AccountServiceApplicationTests.getAccountDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl service;

    @Mock
    private AccountRepository repository;

    @Mock
    private AccountMapperImpl mapper;

    @Mock
    private Page pageResult;

    @Test
    void getAccounts() {
        Page<Account> pagedResponse = new PageImpl(Arrays.asList(getAccount(),getAccount()));
        when(repository.findAll(any(Pageable.class))).thenReturn(pagedResponse);
        when(mapper.mapToDto(any())).thenReturn(Arrays.asList(getAccountDto(),getAccountDto()));

        List<AccountDto> transactions = service.getAccounts(1,1);

        assertEquals("1234", transactions.get(0).getCustomerId());
        assertEquals(AccountType.SAVINGS, transactions.get(1).getType());
    }

    @Test
    void getAccounts_failure() {
        Page<Account> pagedResponse = new PageImpl(Arrays.asList());
        when(repository.findAll(any(Pageable.class))).thenReturn(pagedResponse);

        List<AccountDto> transactions = service.getAccounts(1,1);

        assertEquals(0, transactions.size());
    }

    @Test
    void getAccountByUserId() {
        Page<Account> pagedResponse = new PageImpl(Arrays.asList(getAccount(),getAccount()));
        when(repository.findByCustomerId(any(Pageable.class),eq("1234"))).thenReturn(pagedResponse);
        when(mapper.mapToDto(any())).thenReturn(Arrays.asList(getAccountDto(),getAccountDto()));

        List<AccountDto> transactions = service.getAccountByUserId(1,1,"1234");

        assertEquals("1234", transactions.get(0).getCustomerId());
        assertEquals(AccountType.SAVINGS, transactions.get(1).getType());
    }

    @Test
    void getAccountByUserId_failure() {
        Page<Account> pagedResponse = new PageImpl(Arrays.asList());
        when(repository.findByCustomerId(any(Pageable.class),eq("1234"))).thenReturn(pagedResponse);

        List<AccountDto> transactions = service.getAccountByUserId(1,1,"1234");

        assertEquals(0, transactions.size());
    }

    @Test
    void createAccount() {
        when(mapper.map(any(AccountDto.class))).thenReturn(getAccount());
        when(mapper.map(any(Account.class))).thenReturn(getAccountDto());
        when(repository.save(any())).thenReturn(getAccount());

        AccountDto transactionDto = service.createAccount("1234",getAccountDto());

        assertSame(transactionDto.getCustomerId(), getAccount().getCustomerId());
    }

    @Test
    void createAccount_failure() {
        Throwable error = assertThrows(PathParamsVsInputParamsMismatchException.class,()->service.createAccount("1233",getAccountDto()));  //NOSONAR
        assertEquals("Customer Id-1234 not found. Cannot create account.",error.getMessage());
    }

    @Test
    void getAccountDetailsById() {
        when(repository.findById("123")).thenReturn(Optional.of(getAccount()));
        when(mapper.map(any(Account.class))).thenReturn(getAccountDto());

        AccountDto transactionDto = service.getAccountDetailsById("123");

        assertSame(transactionDto.getType(),getAccountDto().getType());
    }

    @Test
    void updateAccountDetails() {
        when(repository.findById("123")).thenReturn(Optional.of(getAccount()));
        when(repository.save(any())).thenReturn(getAccount());
        when(mapper.map(any(Account.class))).thenReturn(getAccountDto());

        AccountDto accountDto = service.updateAccountDetails("1234","123",getAccountDto());

        assertSame(accountDto.getType(),getAccountDto().getType());
    }

    @Test
    void updateAccountDetails_AccountIdNotFound() {
        when(repository.findById("123")).thenReturn(Optional.empty());

        Throwable error = assertThrows(AccountNotFoundException.class,()->service.updateAccountDetails("1234","123",getAccountDto()));  //NOSONAR
        assertEquals("Account not found for Id-123",error.getMessage());
    }

    @Test
    void updateAccountDetails_CustomerIdMismatch() {

        Throwable error = assertThrows(PathParamsVsInputParamsMismatchException.class,()->service.updateAccountDetails("123","123",getAccountDto())); //NOSONAR
        assertEquals("Customer Id not found! Cannot update account.",error.getMessage());
    }

    @Test
    void deleteAccount() {
        when(repository.findById("123")).thenReturn(Optional.of(getAccount()));
        willDoNothing().given(repository).deleteById("123");

        String transactionDto = service.deleteAccount("123");

        assertSame( "Account deleted successfully.",transactionDto);
    }

    @Test
    void deleteAccount_failure() {
        Throwable error = assertThrows(AccountNotFoundException.class,()->service.deleteAccount("1234"));
        assertEquals("Account not found for Id-1234",error.getMessage());
    }
}