package com.url_Shortner.demo.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    private String Username;
    private String Password;
}
