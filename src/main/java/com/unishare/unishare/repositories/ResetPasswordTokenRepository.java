package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.PasswordResetToken;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // find a valid (unused, not expired) token by its hash and user
    @Query("""
        SELECT t FROM PasswordResetToken t
            WHERE t.codeHash = :codeHash
            AND t.user.id = :userId
            AND t.used = false
            AND t.expiresAt > :now
          """)
    Optional<PasswordResetToken> findValidToken(
            @Param("codeHash") String codeHash,
            @Param("userId") Long userId,
            @Param("now")LocalDateTime now
            );


    // invalidate all previous unused tokens for a user before issuing a new one
    @Modifying
    @Transactional
    @Query("""
            UPDATE PasswordResetToken t
            SET t.used = true
            WHERE t.user.id = :userId
            AND t.used = false
           
            """)
    void invalidatePreviousToken(@Param("userId") Long userId);


}
