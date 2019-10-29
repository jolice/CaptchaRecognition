package io.riguron.captcha;

import io.riguron.captcha.exception.IllegalCaptchaStateException;
import io.riguron.captcha.response.CaptchaGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CaptchaGetController {

    private static final CaptchaGetResponse EMPTY_RESPONSE = new CaptchaGetResponse();

    private CaptchaDistributionService captchaService;

    private UserRequestHolder userRequestHolder;

    @Autowired
    public CaptchaGetController(CaptchaDistributionService captchaService, UserRequestHolder userRequestHolder) {
        this.captchaService = captchaService;
        this.userRequestHolder = userRequestHolder;
    }

    @GetMapping("/get")
    public CaptchaGetResponse getNextCaptcha() {
        try {
            return captchaService.takeNextCaptcha(userRequestHolder.getUserProfile().getId())
                    .map(captcha -> new CaptchaGetResponse(captcha.getType(), captcha.getContents()))
                    .orElse(EMPTY_RESPONSE);
        } catch (IllegalCaptchaStateException e) {
            log.error("Captured exception trying to take next captcha", e);
            return EMPTY_RESPONSE;
        }
    }


}
