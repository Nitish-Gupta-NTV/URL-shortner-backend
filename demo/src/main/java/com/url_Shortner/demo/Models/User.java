package com.url_Shortner.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="user" ,uniqueConstraints = {
@UniqueConstraint(columnNames = "userName"),
@UniqueConstraint(columnNames = "email")
})
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
/*
add this through claud
,uniqueConstraints = {
@UniqueConstraint(columnNames = "userName"),
@UniqueConstraint(columnNames = "email")
}
 */
