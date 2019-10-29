package io.riguron.captcha.input.image;

import io.riguron.captcha.input.image.upload.MultipartCaptchaUpload;
import io.riguron.captcha.response.CaptchaAddResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping(value = "/add", params = "method=post", headers = "Content-Type=multipart/form-data")
    public CaptchaAddResponse acceptImage(MultipartFile file, @RequestParam(name = "callbackUrl", required = false) String callbackUrl) {
        return this.imageCaptchaRegistration.produce(new MultipartCaptchaUpload(file), callbackUrl);
    }

}
