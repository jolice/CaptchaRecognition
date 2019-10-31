package io.riguron.captcha.controller;

import io.riguron.captcha.response.CaptchaSolveResponse;
import io.riguron.captcha.exception.InactiveCaptchaException;
import io.riguron.captcha.exception.InvalidSolverException;
import io.riguron.captcha.solve.CaptchaSolution;
import io.riguron.captcha.solve.CaptchaSolvingService;
import io.riguron.captcha.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CaptchaSolveController {

    private CaptchaSolvingService captchaSolvingService;

    @Autowired
    public CaptchaSolveController(CaptchaSolvingService captchaSolvingService) {
        this.captchaSolvingService = captchaSolvingService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/solve")
    public CaptchaSolveResponse solve(@RequestParam int captchaId, @RequestParam String solution, @AuthenticationPrincipal UserData userData) {
        try {
            captchaSolvingService.solveCaptcha(new CaptchaSolution(captchaId, userData.getId(), solution));
            return new CaptchaSolveResponse(true, "Solution accepted");
        } catch (InactiveCaptchaException e) {
            return new CaptchaSolveResponse(false, "Captcha has been already solved or marked as unsolved");
        } catch (InvalidSolverException e) {
            return new CaptchaSolveResponse(false, "Captcha is being solved by another user or time for solution has expired");
        }

    }

}
