package io.riguron.captcha.input.image.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class MultipartCaptchaUpload implements CaptchaUpload {

    private MultipartFile multipartFile;

    public MultipartCaptchaUpload(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    @Override
    public byte[] upload() throws IOException {
        return multipartFile.getBytes();
    }
}
