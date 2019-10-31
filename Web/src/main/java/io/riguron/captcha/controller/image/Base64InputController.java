package io.riguron.captcha.controller.image;

import io.riguron.captcha.controller.image.upload.Base64CaptchaUpload;
import io.riguron.captcha.response.CaptchaAddResponse;
import io.riguron.captcha.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/add", params = "method=base64")
    public CaptchaAddResponse acceptImage(@RequestParam(name = "body") String body, @RequestParam(name = "callbackUrl", required = false) String callbackUrl, @AuthenticationPrincipal UserData userData) {
         return imageCaptchaRegistration.produce(new Base64CaptchaUpload(body), callbackUrl, userData.getId());
    }

}
