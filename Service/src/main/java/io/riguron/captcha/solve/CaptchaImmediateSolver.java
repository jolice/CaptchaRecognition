package io.riguron.captcha.solve;

import io.riguron.captcha.exception.CaptchaNotFoundException;
import io.riguron.captcha.repository.CaptchaRepository;
import io.riguron.captcha.type.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaptchaImmediateSolver {

    private CaptchaRepository captchaRepository;
    private CaptchaSolvingValidator captchaSolvingValidator;
    private SuccessfulSolving successfulSolving;

    @Autowired
    public CaptchaImmediateSolver(CaptchaRepository captchaRepository, CaptchaSolvingValidator captchaSolvingValidator, SuccessfulSolving successfulSolving) {
        this.captchaRepository = captchaRepository;
        this.captchaSolvingValidator = captchaSolvingValidator;
        this.successfulSolving = successfulSolving;
    }

    public void solveImmediately(CaptchaSolution captchaSolution) {
        Captcha captchaToBeSolved = captchaRepository.findById(captchaSolution.getCaptchaId()).orElseThrow(CaptchaNotFoundException::new);
        this.captchaSolvingValidator.validateBeforeAcceptingSolution(captchaToBeSolved, captchaSolution.getSolverId());
        this.successfulSolving.successfulRecognition(captchaToBeSolved, captchaSolution.getSolution());
    }
}
