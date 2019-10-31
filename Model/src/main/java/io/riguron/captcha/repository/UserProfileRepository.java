package io.riguron.captcha.repository;

import io.riguron.captcha.user.UserProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {

    @Query("SELECT new io.riguron.captcha.repository.Credentials(u.id, u.login, u.password) from UserProfile u WHERE u.login = :login")
    Optional<Credentials> getCredentials(@Param("login") String login);

    boolean existsByLogin(String login);
}
