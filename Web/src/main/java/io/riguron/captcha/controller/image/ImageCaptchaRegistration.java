package io.riguron.captcha.controller.image;

import io.riguron.captcha.CaptchaService;
import io.riguron.captcha.controller.image.upload.CaptchaUpload;
import io.riguron.captcha.response.CaptchaAddResponse;
import io.riguron.captcha.type.Captcha;
import io.riguron.captcha.type.NormalCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ImageCaptchaRegistration {

    private CaptchaService captchaService;

    @Autowired
    public ImageCaptchaRegistration(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    public CaptchaAddResponse produce(CaptchaUpload captchaUpload, String callbackUrl, int userId) {
        return this.addOrFail(captchaUpload, callbackUrl, userId);
    }

    private CaptchaAddResponse addOrFail(CaptchaUpload uploadStrategy, String callbackUrl, int id) {
        try {
            return this.add(uploadStrategy.upload(), callbackUrl, id);
        } catch (IOException e) {
            log.error("Failed to input image", e);
            return new CaptchaAddResponse(false, 0);
        }
    }

    private CaptchaAddResponse add(byte[] data, String callbackUrl, int id) {
        return new CaptchaAddResponse(true, captchaService.addCaptcha(new NormalCaptcha(id, callbackUrl, data)));
    }
}
