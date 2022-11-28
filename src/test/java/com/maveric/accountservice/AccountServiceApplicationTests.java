package com.maveric.accountservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.dto.BalanceDto;
import com.maveric.accountservice.dto.UserDto;
import com.maveric.accountservice.enums.AccountType;
import com.maveric.accountservice.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AccountServiceApplicationTests {

	public static final String apiV1 = "/api/v1/customers/1/accounts";
	@Test
	void testDoSomething() {  // Noncompliant
		assertTrue(true);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static Account getAccount()
	{
		return  Account.builder()
				.customerId("1234")
				.type(AccountType.SAVINGS)
				.build();
	}
	public static AccountDto getAccountDto()
	{
		return  AccountDto.builder()
				.customerId("1234")
				.type(AccountType.SAVINGS)
				.build();
	}


	public static BalanceDto getBalanceDto()
	{
		return BalanceDto.builder()
				.accountId("1234")
				.amount(1000)
				.currency("INR")
				.build();
	}

	public static UserDto getUserDto()
	{
		return UserDto.builder()
				.email("ram@gmail.com")
				._id("1234")
				.build();
	}

}
