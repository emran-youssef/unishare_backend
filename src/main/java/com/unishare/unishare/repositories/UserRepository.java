package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String Email);
    Optional<User> findByUniversityEmail(String universityEmail);

    boolean existsByEmail(String email);
    boolean existsByUniversityEmail(String universityEmail);


}
