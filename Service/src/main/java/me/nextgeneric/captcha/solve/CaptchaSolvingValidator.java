package me.nextgeneric.captcha.solve;

import me.nextgeneric.captcha.CaptchaStatus;
import me.nextgeneric.captcha.exception.InactiveCaptchaException;
import me.nextgeneric.captcha.exception.InvalidSolverException;
import me.nextgeneric.captcha.type.Captcha;
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
