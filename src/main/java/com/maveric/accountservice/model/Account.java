package com.maveric.accountservice.model;

import com.maveric.accountservice.enums.AccountType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Account")
public class Account {

    @Id
    private String _id;
    private String customerId;
    private AccountType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
