package com.maveric.accountservice.controller;

import com.maveric.accountservice.dto.BalanceDto;
import com.maveric.accountservice.dto.UserDto;
import com.maveric.accountservice.exception.CustomerNotFoundException;
import com.maveric.accountservice.exception.UnAuthorizedException;
import com.maveric.accountservice.feignconsumer.BalanceServiceConsumer;
import com.maveric.accountservice.feignconsumer.TransactionServiceConsumer;
import com.maveric.accountservice.feignconsumer.UserServiceConsumer;
import com.maveric.accountservice.service.AccountService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;

import java.util.Arrays;
import java.util.List;

import static com.maveric.accountservice.AccountServiceApplicationTests.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes=AccountController.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mock;

    @MockBean
    private AccountService accountService;

    @MockBean
    BalanceServiceConsumer balanceServiceConsumer;

    @MockBean
    TransactionServiceConsumer transactionServiceConsumer;

    @MockBean
    UserServiceConsumer userServiceConsumer;

    @Mock
    ResponseEntity<BalanceDto> balanceDto;



    @Test
    void getAccounts() throws Exception{
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(getUserDto(), HttpStatus.OK);
        mock.perform(get("/api/v1/customers/1/account")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getAccountByCustomerId() throws Exception {
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(getUserDto(), HttpStatus.OK);
        when(userServiceConsumer.getUserDetails(any(String.class))).thenReturn(responseEntity);
        mock.perform(get(apiV1)
                        .contentType(MediaType.APPLICATION_JSON).header("userEmail", "ram@gmail.com"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    void getAccountByCustomerId_failure() throws Exception {
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(new UserDto(), HttpStatus.OK);
        when(userServiceConsumer.getUserDetails(any(String.class))).thenReturn(responseEntity);

        Throwable error = assertThrows(NestedServletException.class,()->mock.perform(get(apiV1)
                        .contentType(MediaType.APPLICATION_JSON).header("userEmail", "ram@gmail.com")).andReturn());
    }

    @Test
    void createAccount() throws Exception{
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(getUserDto(), HttpStatus.OK);
        when(userServiceConsumer.getUserDetails(any(String.class))).thenReturn(responseEntity);
        mock.perform(post(apiV1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(getAccountDto()))
                        .header("userEmail", "ram@gmail.com")
                )
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void createAccount_failure() throws Exception{
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(new UserDto(), HttpStatus.OK);
        when(userServiceConsumer.getUserDetails(any(String.class))).thenReturn(responseEntity);
        Throwable error = assertThrows(NestedServletException.class,()->mock.perform(post(apiV1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getAccountDto()))
                .header("userEmail", "ram@gmail.com")
        ).andReturn());
    }


    @Test
    void getAccountDetails() throws Exception{
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(getUserDto(), HttpStatus.OK);
        when(userServiceConsumer.getUserDetails(any(String.class))).thenReturn(responseEntity);
        when(accountService.getAccountDetailsById(any(String.class))).thenReturn(getAccountDto());
        when(balanceServiceConsumer.getBalances(any(String.class))).thenReturn(balanceDto);
        when(balanceDto.getBody()).thenReturn(getBalanceDto());

        mock.perform(get(apiV1+"/accountId1").header("userEmail", "ram@gmail.com"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void updateAccount() throws Exception{
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(getUserDto(), HttpStatus.OK);
        when(userServiceConsumer.getUserDetails(any(String.class))).thenReturn(responseEntity);
        mock.perform(put(apiV1+"/accountId1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(getAccountDto()))
                        .header("userEmail", "ram@gmail.com")
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void updateAccount_failure() throws Exception{
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(new UserDto(), HttpStatus.OK);
        when(userServiceConsumer.getUserDetails(any(String.class))).thenReturn(responseEntity);
        Throwable error = assertThrows(NestedServletException.class,()->mock.perform(put(apiV1+"/accountId1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(getAccountDto()))
                .header("userEmail", "ram@gmail.com")
        ).andReturn());
    }

    @Test
    void deleteAccount() throws Exception {
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(getUserDto(), HttpStatus.OK);
        when(userServiceConsumer.getUserDetails(any(String.class))).thenReturn(responseEntity);
        mock.perform(delete(apiV1+"/accountId1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("userEmail", "ram@gmail.com"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteAccount_failure() throws Exception {
        ResponseEntity<UserDto> responseEntity = new ResponseEntity<>(new UserDto(), HttpStatus.OK);
        when(userServiceConsumer.getUserDetails(any(String.class))).thenReturn(responseEntity);
        Throwable error = assertThrows(NestedServletException.class,()->mock.perform(delete(apiV1+"/accountId1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("userEmail", "ram@gmail.com")).andReturn());
    }

}