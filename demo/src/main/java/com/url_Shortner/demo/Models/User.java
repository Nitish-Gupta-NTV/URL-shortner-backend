package com.url_Shortner.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;
    private  String roleUser ="Role_User";
    private String email;

}
