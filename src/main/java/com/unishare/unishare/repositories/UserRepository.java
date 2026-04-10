package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String Email);
    Optional<User> findByUniversityEmail(String universityEmail);

    boolean existsByEmail(String email);
    boolean existsByUniversityEmail(String universityEmail);


}
