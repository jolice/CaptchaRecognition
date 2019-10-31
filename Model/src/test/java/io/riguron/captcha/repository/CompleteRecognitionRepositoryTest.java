package io.riguron.captcha.repository;

import io.riguron.captcha.user.CompleteRecognition;
import io.riguron.captcha.user.UserProfile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
public class CompleteRecognitionRepositoryTest extends RepositoryTest {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private CompleteRecognitionRepository completeRecognitionRepository;

    @Test
    public void findByUserId() {
        UserProfile userProfile = new UserProfile("login", "password");

        userProfileRepository.save(userProfile);
        completeRecognitionRepository.save(
                new CompleteRecognition(userProfile, 3, 3, 3));

        Optional<CompleteRecognition> result = completeRecognitionRepository.findByUserId(userProfile.getId());
        assertTrue(result.isPresent());
        CompleteRecognition completeRecognition = result.get();
        assertEquals(userProfile.getId(), completeRecognition.getId());
    }

}