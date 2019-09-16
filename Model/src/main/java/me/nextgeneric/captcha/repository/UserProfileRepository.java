package me.nextgeneric.captcha.repository;

import me.nextgeneric.captcha.user.UserProfile;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserProfile u WHERE u.id = :userId")
    UserProfile retrieveForUpdate(Integer userId);

    Optional<UserProfile> findOneByApiKey(String apiKey);

    boolean existsByLogin(String login);
}
