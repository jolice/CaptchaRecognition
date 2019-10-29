package io.riguron.captcha.input.text;

import io.riguron.captcha.CaptchaService;
import io.riguron.captcha.UserRequestHolder;
import io.riguron.captcha.response.CaptchaAddResponse;
import io.riguron.captcha.type.TextCaptcha;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextInputController {

    private UserRequestHolder userRequestHolder;
    private CaptchaService captchaService;

    public TextInputController(UserRequestHolder userRequestHolder, CaptchaService captchaService) {
        this.userRequestHolder = userRequestHolder;
        this.captchaService = captchaService;
    }

    @PostMapping(value = "/add", params = "text")
    public CaptchaAddResponse acceptTextCaptcha(@RequestParam(name = "callbackUrl", required = false) String callbackUrl, String text) {
        int id = this.captchaService.addCaptcha(new TextCaptcha(userRequestHolder.getUserProfile().getId(), callbackUrl, text));
        return new CaptchaAddResponse(true, id);
    }
}
