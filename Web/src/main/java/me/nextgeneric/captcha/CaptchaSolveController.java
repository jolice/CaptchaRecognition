package me.nextgeneric.captcha;

import me.nextgeneric.captcha.exception.InactiveCaptchaException;
import me.nextgeneric.captcha.exception.InvalidSolverException;
import me.nextgeneric.captcha.response.CaptchaSolveResponse;
import me.nextgeneric.captcha.solve.CaptchaSolution;
import me.nextgeneric.captcha.solve.CaptchaSolvingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CaptchaSolveController {

    private UserRequestHolder userRequestHolder;

    private CaptchaSolvingService captchaSolvingService;

    public CaptchaSolveController(UserRequestHolder userRequestHolder, CaptchaSolvingService captchaSolvingService) {
        this.userRequestHolder = userRequestHolder;
        this.captchaSolvingService = captchaSolvingService;
    }

    @PostMapping("/solve")
    public CaptchaSolveResponse solve(int captchaId, String solution) {
        try {
            captchaSolvingService.solveCaptcha(new CaptchaSolution(captchaId, userRequestHolder.getUserProfile().getId(), solution));
            return new CaptchaSolveResponse(true, "Solution accepted");
        } catch (InactiveCaptchaException e) {
            return new CaptchaSolveResponse(false, "Captcha has been already solved or marked as unsolved");
        } catch (InvalidSolverException e) {
            return new CaptchaSolveResponse(false, "Captcha is being solved by another user or time for solution has expired");
        }

    }

}
