package io.riguron.captcha.controller;

import io.riguron.captcha.CaptchaDistributionService;
import io.riguron.captcha.exception.IllegalCaptchaStateException;
import io.riguron.captcha.response.CaptchaGetResponse;
import io.riguron.captcha.response.Response;
import io.riguron.captcha.type.Captcha;
import io.riguron.captcha.user.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CaptchaGetController {


    private CaptchaDistributionService captchaService;

    @Autowired
    public CaptchaGetController(CaptchaDistributionService captchaService) {
        this.captchaService = captchaService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get")
    public Response getNextCaptcha(@AuthenticationPrincipal UserData userData) {
        try {
            return captchaService.takeNextCaptcha(userData.getId())
                    .map(this::success)
                    .orElse(new Response(false));
        } catch (IllegalCaptchaStateException e) {
            log.error("Captured exception trying to take next captcha", e);
            return new Response(false);
        }
    }

    private Response success(Captcha captcha) {
        return new CaptchaGetResponse(captcha.getType(), captcha.getContents());
    }





}
