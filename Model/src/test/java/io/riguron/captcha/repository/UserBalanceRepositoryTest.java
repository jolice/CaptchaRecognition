package io.riguron.captcha.repository;

import io.riguron.captcha.user.UserBalance;
import io.riguron.captcha.user.UserProfile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@Transactional
public class UserBalanceRepositoryTest extends RepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Test
    public void findByUserId() {
        UserProfile userProfile = new UserProfile("login", "password");

        userProfileRepository.save(userProfile);
        userBalanceRepository.save(new UserBalance(userProfile));
        assertEquals(userProfile.getId(), userBalanceRepository.findByUserId(userProfile.getId()).getId());
    }
}