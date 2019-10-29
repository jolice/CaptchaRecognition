package io.riguron.captcha.solve;

import io.riguron.captcha.CaptchaStatus;
import io.riguron.captcha.exception.InactiveCaptchaException;
import io.riguron.captcha.exception.InvalidSolverException;
import io.riguron.captcha.type.Captcha;
import org.springframework.stereotype.Component;

@Component
public class CaptchaSolvingValidator {

    public void validateBeforeAcceptingSolution(Captcha captchaToBeSolved, int solverId) {

        if (captchaToBeSolved.getCaptchaStatus() != CaptchaStatus.PROCESSING) {
            throw new InactiveCaptchaException();
        }

        if (captchaToBeSolved.hasNotSolver(solverId)) {
            throw new InvalidSolverException();
        }
    }

}
