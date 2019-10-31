package io.riguron.captcha;

import io.riguron.captcha.user.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@Profile("dev")
public class DatabaseSeeder {



    private UserProfileService userProfileRepository;

    @Autowired
    public DatabaseSeeder(UserProfileService userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @PostConstruct
    public void seed() {
        userProfileRepository.register("next", "password");
    }
}
