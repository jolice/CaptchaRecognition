package me.nextgeneric.captcha.input.image;

import lombok.extern.slf4j.Slf4j;
import me.nextgeneric.captcha.CaptchaService;
import me.nextgeneric.captcha.UserRequestHolder;
import me.nextgeneric.captcha.input.image.upload.CaptchaUpload;
import me.nextgeneric.captcha.response.CaptchaAddResponse;
import me.nextgeneric.captcha.type.Captcha;
import me.nextgeneric.captcha.type.NormalCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ImageCaptchaRegistration {

    private UserRequestHolder userRequestHolder;
    private CaptchaService captchaService;

    @Autowired
    public ImageCaptchaRegistration(UserRequestHolder userRequestHolder, CaptchaService captchaService) {
        this.userRequestHolder = userRequestHolder;
        this.captchaService = captchaService;
    }

    public CaptchaAddResponse produce(CaptchaUpload captchaUpload, String callbackUrl) {
        return this.addOrFail(captchaUpload, callbackUrl);
    }

    private CaptchaAddResponse addOrFail(CaptchaUpload uploadStrategy, String callbackUrl) {
        byte[] data;
        try {
            data = uploadStrategy.upload();
        } catch (IOException e) {
            log.error("Failed to input image", e);
            return new CaptchaAddResponse(false, 0);
        }
        return this.add(data, callbackUrl);
    }

    private CaptchaAddResponse add(byte[] data, String callbackUrl) {
        Captcha newCaptcha = new NormalCaptcha(userRequestHolder.getUserProfile().getId(), callbackUrl, data);
        int createdCaptchaId = captchaService.addCaptcha(newCaptcha);
        return new CaptchaAddResponse(true, createdCaptchaId);
    }
}
