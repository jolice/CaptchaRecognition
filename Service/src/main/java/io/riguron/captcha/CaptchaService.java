package io.riguron.captcha;

import lombok.extern.slf4j.Slf4j;
import io.riguron.captcha.repository.CaptchaRepository;
import io.riguron.captcha.repository.UserProfileRepository;
import io.riguron.captcha.type.Captcha;
import io.riguron.captcha.user.UserNotFoundException;
import io.riguron.captcha.user.UserProfile;
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
