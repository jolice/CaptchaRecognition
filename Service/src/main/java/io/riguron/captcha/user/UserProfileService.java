package io.riguron.captcha.user;

import io.riguron.captcha.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserProfileService {

    private UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional(readOnly = true)
    public Optional<UserProfile> findByToken(String apiKey) {
        return userProfileRepository.findOneByApiKey(apiKey);
    }

    @Transactional
    public UserProfile register(String login) {

        if (userProfileRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("User with such login already exists");
        }

        String apiKey = UUID.randomUUID().toString().replace("-", "");
        UserProfile userProfile = new UserProfile(login, apiKey);
        return userProfileRepository.save(userProfile);
    }

}
