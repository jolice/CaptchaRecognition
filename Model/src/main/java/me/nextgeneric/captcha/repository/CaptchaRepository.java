package me.nextgeneric.captcha.repository;

import me.nextgeneric.captcha.type.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

public interface CaptchaRepository extends JpaRepository<Captcha, Integer> {

    @Modifying
    @Transactional
    void deleteByRegistrationDateBefore(Timestamp expiryDate);

    @Query("SELECT c from Captcha c LEFT JOIN FETCH c.solutions WHERE c.id = :captchaId")
    Optional<Captcha> findByIdWithSolutions(@Param("captchaId") int captchaId);

    @Query("SELECT c from Captcha c LEFT JOIN FETCH c.solvers WHERE c.id = :captchaId")
    Optional<Captcha> findByIdWithSolvers(@Param("captchaId") int captchaId);

}
