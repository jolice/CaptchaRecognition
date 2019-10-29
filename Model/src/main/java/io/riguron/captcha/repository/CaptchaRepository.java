package io.riguron.captcha.repository;

import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphs;
import io.riguron.captcha.type.Captcha;
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

    Optional<Captcha> findById(@Param("captchaId") int captchaId, EntityGraph entityGraph);

}
