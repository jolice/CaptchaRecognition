package io.riguron.captcha.controller.image;

import io.riguron.captcha.controller.image.upload.MultipartCaptchaUpload;
import io.riguron.captcha.response.CaptchaAddResponse;
import io.riguron.captcha.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MultipartInputController {

    private ImageCaptchaRegistration imageCaptchaRegistration;

    @Autowired
    public MultipartInputController(ImageCaptchaRegistration imageCaptchaRegistration) {
        this.imageCaptchaRegistration = imageCaptchaRegistration;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/add", params = "method=post", headers = "Content-Type=multipart/form-data")
    public CaptchaAddResponse acceptImage(MultipartFile file, @RequestParam(name = "callbackUrl", required = false) String callbackUrl, @AuthenticationPrincipal UserData userData) {
        return this.imageCaptchaRegistration.produce(new MultipartCaptchaUpload(file), callbackUrl, userData.getId());
    }

}
