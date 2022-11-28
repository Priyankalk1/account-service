package com.maveric.accountservice.controller;

import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.dto.BalanceDto;
import com.maveric.accountservice.dto.UserDto;
import com.maveric.accountservice.exception.CustomerNotFoundException;
import com.maveric.accountservice.exception.UnAuthorizedException;
import com.maveric.accountservice.feignconsumer.BalanceServiceConsumer;
import com.maveric.accountservice.feignconsumer.TransactionServiceConsumer;
import com.maveric.accountservice.feignconsumer.UserServiceConsumer;
import com.maveric.accountservice.service.AccountService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.maveric.accountservice.constants.Constants.AUTH_HEADER_ERROR_MESSAGE;


@RestController
@RequestMapping("/api/v1")
public class AccountController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountService accountService;
    @Autowired
    BalanceServiceConsumer balanceServiceConsumer;
    @Autowired
    TransactionServiceConsumer transactionServiceConsumer;

    @Autowired
    UserServiceConsumer userServiceConsumer;

    /* Dummy Returns list of all accounts */
    @GetMapping("customers/{customerId}/account")
    public ResponseEntity<List<AccountDto>> getAccounts(@PathVariable String customerId,@RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        List<AccountDto> accountDtoResponse = accountService.getAccounts(page,pageSize);
        return new ResponseEntity<>(accountDtoResponse, HttpStatus.OK);
    }

    /* Returns list of all valid accounts belonging a particular Customer Id */
    @GetMapping("customers/{customerId}/accounts")
    public ResponseEntity<List<AccountDto>> getAccountByCustomerId(@PathVariable String customerId,@RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "5") Integer pageSize,@RequestHeader String userEmail) {
        log.info("API call for retrieving list of valid accounts belonging a particular Customer Id");
        ResponseEntity<UserDto> responseEntityUserDto = userServiceConsumer.getUserDetails(customerId);
        UserDto userDto = responseEntityUserDto.getBody()==null?new UserDto():responseEntityUserDto.getBody(); //NOSONAR
        if (userDto.get_id() != null) { //NOSONAR
            if (userDto.getEmail().equalsIgnoreCase(userEmail)) {
                List<AccountDto> accountDtoResponse = accountService.getAccountByUserId(page, pageSize, customerId);
                return new ResponseEntity<>(accountDtoResponse, HttpStatus.OK);
            } else {
                log.error(AUTH_HEADER_ERROR_MESSAGE);
                throw new UnAuthorizedException(AUTH_HEADER_ERROR_MESSAGE);
            }
        }
        else {
            log.error("Customer Id not found! Cannot retrieve account details");
            throw new CustomerNotFoundException("Account details not available because Customer Id-" + customerId + " not found.");
        }
    }


    /* Creates a valid account */
    @PostMapping("customers/{customerId}/accounts")
    public ResponseEntity<AccountDto> createAccount(@PathVariable String customerId, @Valid @RequestBody AccountDto accountDto,@RequestHeader String userEmail) {
        ResponseEntity<UserDto> responseEntityUserDto = userServiceConsumer.getUserDetails(accountDto.getCustomerId());
        UserDto userDto = responseEntityUserDto.getBody(); //NOSONAR

        log.info("API call to create account for a valid customer");
        if(userDto.get_id()!=null) { //NOSONAR
            if(userDto.getEmail().equalsIgnoreCase(userEmail)) {
                AccountDto accountDtoResponse = accountService.createAccount(customerId, accountDto);
                return new ResponseEntity<>(accountDtoResponse, HttpStatus.CREATED);
            }
            else {
                log.error(AUTH_HEADER_ERROR_MESSAGE);
                throw new UnAuthorizedException(AUTH_HEADER_ERROR_MESSAGE);
            }
        }
        else {
            log.error("Customer Id not found! Cannot create account details");
            throw new CustomerNotFoundException("Customer Id-"+accountDto.getCustomerId()+" not found. Cannot create account.");
        }
    }

    /* Returns Account details along with Balance details based on valid Account Id */
    @GetMapping("customers/{customerId}/accounts/{accountId}")
    public ResponseEntity<AccountDto> getAccountDetails(@PathVariable String customerId,@PathVariable String accountId,@RequestHeader String userEmail) {

        log.info("API call to retrieve account details and its corresponding balance details for valid Account Id and Customer Id");
        ResponseEntity<UserDto> responseEntityUserDto = userServiceConsumer.getUserDetails(customerId);
        UserDto userDto = responseEntityUserDto.getBody()==null?new UserDto():responseEntityUserDto.getBody(); //NOSONAR
        if(userDto.getEmail().equalsIgnoreCase(userEmail)) {  //NOSONAR
            AccountDto accountDtoResponse = accountService.getAccountDetailsById(accountId);
            ResponseEntity<BalanceDto> balanceDto = balanceServiceConsumer.getBalances(accountId);
            accountDtoResponse.setBalance(balanceDto.getBody());
            log.info("Retrieved account details.");
            return new ResponseEntity<>(accountDtoResponse, HttpStatus.OK);
        }
        else
        {
            log.error(AUTH_HEADER_ERROR_MESSAGE);
            throw new UnAuthorizedException(AUTH_HEADER_ERROR_MESSAGE);
        }
    }

    /* Updates account details based on valid Account Id */
    @PutMapping("customers/{customerId}/accounts/{accountId}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable String customerId,@PathVariable String accountId,@RequestBody AccountDto accountDto,@RequestHeader String userEmail) {
        log.info("API call to update account details");
        ResponseEntity<UserDto> responseEntityUserDto = userServiceConsumer.getUserDetails(customerId);
        UserDto userDto = responseEntityUserDto.getBody()==null?new UserDto():responseEntityUserDto.getBody(); //NOSONAR
        if(userDto.getEmail().equalsIgnoreCase(userEmail)) { //NOSONAR
            AccountDto accountDtoResponse = accountService.updateAccountDetails(customerId, accountId, accountDto);
            log.info("Account details updated!");
            return new ResponseEntity<>(accountDtoResponse, HttpStatus.OK);
        }
        else
        {
            log.error(AUTH_HEADER_ERROR_MESSAGE);
            throw new UnAuthorizedException(AUTH_HEADER_ERROR_MESSAGE);
        }

    }

    /* Deletes account along with its corresponding balance and transaction details based on valid Account Id */
    @DeleteMapping("customers/{customerId}/accounts/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable String customerId,@PathVariable String accountId,@RequestHeader String userEmail) {
        log.info("API call to delete account details");
        ResponseEntity<UserDto> responseEntityUserDto = userServiceConsumer.getUserDetails(customerId);
        UserDto userDto = responseEntityUserDto.getBody()==null?new UserDto():responseEntityUserDto.getBody(); //NOSONAR
        if(userDto.getEmail().equalsIgnoreCase(userEmail)) { //NOSONAR
            String result = accountService.deleteAccount(accountId);
            if (result != null) {
                balanceServiceConsumer.deleteBalanceByAccountId(accountId);
                transactionServiceConsumer.deleteTransactionByAccountId(accountId);
            }
            log.info("Account deleted along with its corresponding balance and transaction details");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else
        {
            log.error(AUTH_HEADER_ERROR_MESSAGE);
            throw new UnAuthorizedException(AUTH_HEADER_ERROR_MESSAGE);
        }
    }
}
