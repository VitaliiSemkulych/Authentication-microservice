package com.webbook.webbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDTO {
    private String email;
    private String password;
    private Boolean rememberMe;
}
