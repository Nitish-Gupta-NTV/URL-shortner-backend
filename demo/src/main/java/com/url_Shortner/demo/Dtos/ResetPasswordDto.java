package com.url_Shortner.demo.Dtos;

import lombok.Data;

@Data
public class ResetPasswordDto {
    private String token;
    private String password;
}
