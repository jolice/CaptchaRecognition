package io.riguron.captcha.repository;

import io.riguron.captcha.user.UserProfile;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserProfile u WHERE u.id = :userId")
    UserProfile retrieveForUpdate(Integer userId);

    Optional<UserProfile> findOneByApiKey(String apiKey);

    boolean existsByLogin(String login);
}
