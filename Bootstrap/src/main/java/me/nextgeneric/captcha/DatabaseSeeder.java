package me.nextgeneric.captcha;

import me.nextgeneric.captcha.repository.UserProfileRepository;
import me.nextgeneric.captcha.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@Profile("dev")
public class DatabaseSeeder {

    private UserProfileRepository userProfileRepository;

    @Autowired
    public DatabaseSeeder(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @PostConstruct
    public void seed() {
        UserProfile userProfile = new UserProfile("next", "sigma");
        userProfileRepository.save(userProfile);
    }
}
