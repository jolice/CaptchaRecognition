package me.nextgeneric.captcha.input.text;

import me.nextgeneric.captcha.CaptchaService;
import me.nextgeneric.captcha.UserRequestHolder;
import me.nextgeneric.captcha.response.CaptchaAddResponse;
import me.nextgeneric.captcha.type.TextCaptcha;
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
