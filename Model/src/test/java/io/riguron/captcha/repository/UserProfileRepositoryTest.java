package io.riguron.captcha.repository;
import io.riguron.captcha.user.UserProfile;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.*;


public class UserProfileRepositoryTest extends RepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    public void getCredentials() {

        UserProfile userProfile = new UserProfile("login", "password");
        userProfileRepository.save(userProfile);
        int id = userProfile.getId();

        Optional<Credentials> login = userProfileRepository.getCredentials("login");
        assertTrue(login.isPresent());
        Credentials credentials = login.get();
        assertEquals(id, credentials.getId());
        assertEquals("login", credentials.getLogin());
        assertEquals("password", credentials.getPassword());
    }

    @Test
    public void existsByLogin() {
        assertFalse(userProfileRepository.existsByLogin("login"));
        UserProfile userProfile = new UserProfile("login", "password");
        userProfileRepository.save(userProfile);
        assertTrue(userProfileRepository.existsByLogin("login"));
    }

    @After
    public void cleanUp() {
        userProfileRepository.deleteAll();
    }
}