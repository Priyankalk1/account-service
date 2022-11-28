package com.maveric.accountservice.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class ErrorDto {
    String code;
    String message;
}
