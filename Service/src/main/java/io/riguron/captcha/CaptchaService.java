package io.riguron.captcha;

import io.riguron.captcha.queue.IdentifierQueue;
import io.riguron.captcha.repository.CaptchaRepository;
import io.riguron.captcha.repository.CompleteRecognitionRepository;
import io.riguron.captcha.type.Captcha;
import io.riguron.captcha.user.CompleteRecognition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
public class CaptchaService {

    private CompleteRecognitionRepository completeRecognitionRepository;
    private CaptchaRepository captchaRepository;
    private IdentifierQueue identifierQueue;

    @Autowired
    public CaptchaService(CompleteRecognitionRepository completeRecognitionRepository, CaptchaRepository captchaRepository, IdentifierQueue identifierQueue) {
        this.completeRecognitionRepository = completeRecognitionRepository;
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
        return completeRecognitionRepository.findByUserId(captcha.getOriginatorId())
                .map(CompleteRecognition::getMinimumAttempts)
                .orElse(1);
    }

    @Transactional
    public Optional<Captcha> getCaptcha(final int captchaId) {
        return captchaRepository.findById(captchaId);
    }


}
