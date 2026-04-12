package com.url_Shortner.demo.Repository;

import com.url_Shortner.demo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String username);
    boolean existsByUserName(String userName);  // claud
    boolean existsByEmail(String email); //claud
}
