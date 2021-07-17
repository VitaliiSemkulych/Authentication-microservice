package com.webbook.webbook.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDTO {
    private String userName;
    private String email;
    private String phoneNumber;

}
