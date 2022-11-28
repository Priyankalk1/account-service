package com.maveric.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.maveric.accountservice.enums.AccountType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
        private String _id;
        @NotBlank(message = "Customer Id is mandatory")
        private String customerId;
        @NotNull(message = "Type is mandatory - 'SAVINGS' or 'CURRENT'")
        private AccountType type;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BalanceDto balance;
}
