package com.maveric.accountservice.service;

import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.exception.AccountNotFoundException;
import com.maveric.accountservice.exception.PathParamsVsInputParamsMismatchException;
import com.maveric.accountservice.mapper.AccountMapper;
import com.maveric.accountservice.model.Account;
import com.maveric.accountservice.repository.AccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.maveric.accountservice.constants.Constants.*;
import static com.maveric.accountservice.util.Common.getCurrentDateTime;

@Service
public class AccountServiceImpl implements AccountService{

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository repository;

    @Autowired
    private AccountMapper mapper;

    @Override
    public List<AccountDto> getAccounts(Integer page, Integer pageSize) {
        Pageable paging = PageRequest.of(page, pageSize);
        Page<Account> pageResult = repository.findAll(paging);
        if(pageResult.hasContent()) {
            List<Account> account = pageResult.getContent();
            return mapper.mapToDto(account);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<AccountDto> getAccountByUserId(Integer page, Integer pageSize, String userId) {
        Pageable paging = PageRequest.of(page, pageSize);
        Page<Account> pageResult = repository.findByCustomerId(paging,userId);
        if(pageResult.hasContent()) {
            List<Account> account = pageResult.getContent();
            log.info("Retrieved list of accounts from DB");
            return mapper.mapToDto(account);
        } else {
            log.info("No account details found!");
            return new ArrayList<>();
        }
    }

    @Override
    public AccountDto createAccount(String customerId,AccountDto accountDto) {
        if(customerId.equalsIgnoreCase(accountDto.getCustomerId())) {
            //Adding Time
            accountDto.setCreatedAt(getCurrentDateTime());
            accountDto.setUpdatedAt(getCurrentDateTime());

            Account account = mapper.map(accountDto);
            Account accountResult = repository.save(account);
            return mapper.map(accountResult);
        }
        else {
            log.error("Customer not found! Cannot create Account.");
            throw new PathParamsVsInputParamsMismatchException("Customer Id-"+accountDto.getCustomerId()+" not found. Cannot create account.");
        }
    }

    @Override
    public AccountDto getAccountDetailsById(String accountId) {
        Account accountResult=repository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE+accountId));
        return mapper.map(accountResult);
    }

    @Override
    public AccountDto updateAccountDetails(String customerId,String accountId, AccountDto accountDto) {
        if(customerId.equalsIgnoreCase(accountDto.getCustomerId())) {
            Account accountResult = repository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE + accountId));
            accountResult.set_id(accountResult.get_id());
            accountResult.setCustomerId(accountDto.getCustomerId());
            accountResult.setType(accountDto.getType());
            accountResult.setCreatedAt(accountResult.getCreatedAt());
            accountResult.setUpdatedAt(getCurrentDateTime());
            Account accountUpdated = repository.save(accountResult);
            return mapper.map(accountUpdated);
        }
        else {
            log.error("Customer not found! Cannot update Account.");
            throw new PathParamsVsInputParamsMismatchException("Customer Id not found! Cannot update account.");
        }
    }

    @Override
    public String deleteAccount(String accountId) {
        if(!repository.findById(accountId).isPresent())
        {
            log.error("Account to be deleted is not found.");
            throw new AccountNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE+accountId);
        }
        repository.deleteById(accountId);
        return ACCOUNT_DELETED_SUCCESS;
    }


}
