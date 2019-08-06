package me.nextgeneric.captcha;

import lombok.extern.slf4j.Slf4j;
import me.nextgeneric.captcha.repository.CaptchaRepository;
import me.nextgeneric.captcha.repository.UserProfileRepository;
import me.nextgeneric.captcha.type.Captcha;
import me.nextgeneric.captcha.user.UserNotFoundException;
import me.nextgeneric.captcha.user.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
public class CaptchaService {

    private UserProfileRepository userProfileRepository;
    private CaptchaRepository captchaRepository;
    private IdentifierQueue identifierQueue;

    public CaptchaService(UserProfileRepository userProfileRepository, CaptchaRepository captchaRepository, IdentifierQueue identifierQueue) {
        this.userProfileRepository = userProfileRepository;
        this.captchaRepository = captchaRepository;
        this.identifierQueue = identifierQueue;
    }

    @Transactional
    public int addCaptcha(Captcha captcha) {

        captchaRepository.saveAndFlush(captcha);
        int timesToAdd = this.determineInitialWorkers(captcha);

        for (int i = 0; i < timesToAdd; i++) {
            identifierQueue.pushBack(captcha.getId());
        }

        return captcha.getId();
    }

    private int determineInitialWorkers(Captcha captcha) {
        UserProfile userProfile = userProfileRepository.findById(captcha.getOriginatorId()).orElseThrow(UserNotFoundException::new);
        return userProfile.getCompleteRecognition().isEnabled() ? userProfile.getCompleteRecognition().getMinimumAttempts() : 1;
    }

    @Transactional
    public Optional<Captcha> getCaptcha(final int captchaId) {
        return captchaRepository.findById(captchaId);
    }


}
