package com.test.loan.repo;

import com.test.loan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
    boolean existsUserByUsername(String userName);

    Optional<User> findByUsername(String username);

}
