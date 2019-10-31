package io.riguron.captcha.user;

import io.riguron.captcha.repository.UserBalanceRepository;
import io.riguron.captcha.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserProfileService {

    private PasswordEncoder passwordEncoder;
    private UserProfileRepository userProfileRepository;
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    public UserProfileService(PasswordEncoder passwordEncoder, UserProfileRepository userProfileRepository, UserBalanceRepository userBalanceRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userProfileRepository = userProfileRepository;
        this.userBalanceRepository = userBalanceRepository;
    }

    @Transactional
    public void register(String login, String password) {

        if (userProfileRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("User already exists");
        }

        if (password.length() <= 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        UserProfile userProfile = new UserProfile(login, passwordEncoder.encode(password));
        userProfileRepository.save(userProfile);
        userBalanceRepository.save(new UserBalance(userProfile));
    }

}
