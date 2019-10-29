package io.riguron.captcha.input.image;

import io.riguron.captcha.input.image.upload.Base64CaptchaUpload;
import io.riguron.captcha.response.CaptchaAddResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Base64InputController {

    private ImageCaptchaRegistration imageCaptchaRegistration;

    @Autowired
    public Base64InputController(ImageCaptchaRegistration imageCaptchaRegistration) {
        this.imageCaptchaRegistration = imageCaptchaRegistration;
    }

    @PostMapping(value = "/add", params = "method=base64")
    public CaptchaAddResponse acceptImage(@RequestParam(name = "body") String body, @RequestParam(name = "callbackUrl", required = false) String callbackUrl) {
         return imageCaptchaRegistration.produce(new Base64CaptchaUpload(body), callbackUrl);
    }

}
