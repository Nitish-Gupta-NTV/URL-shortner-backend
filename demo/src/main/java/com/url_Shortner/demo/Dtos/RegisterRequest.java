package com.url_Shortner.demo.Dtos;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String Username;
    private String email;
    private String Password;
    private Set<String> role;

}
