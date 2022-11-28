package com.maveric.accountservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class BalanceDto {
    private String  _id;
    private String accountId;
    private Number amount;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
