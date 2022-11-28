package com.maveric.accountservice.dto;

import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String _id;
    private String email;
}
