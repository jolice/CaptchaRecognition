package io.riguron.captcha.controller.text;

import io.riguron.captcha.CaptchaService;
import io.riguron.captcha.response.CaptchaAddResponse;
import io.riguron.captcha.type.TextCaptcha;
import io.riguron.captcha.user.UserData;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextInputController {

    private CaptchaService captchaService;

    public TextInputController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @PostMapping(value = "/add", params = "method=text")
    @PreAuthorize("isAuthenticated()")
    public CaptchaAddResponse acceptTextCaptcha(@RequestParam(name = "callbackUrl", required = false) String callbackUrl, @RequestParam String text, @AuthenticationPrincipal UserData userData) {
        return new CaptchaAddResponse(true,
                this.captchaService.addCaptcha(new TextCaptcha(userData.getId(), callbackUrl, text)));
    }
}
