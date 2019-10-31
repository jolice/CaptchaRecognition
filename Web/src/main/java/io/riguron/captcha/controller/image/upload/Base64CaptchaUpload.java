package io.riguron.captcha.controller.image.upload;

import java.util.Base64;
import java.util.Optional;

public class Base64CaptchaUpload implements CaptchaUpload {

    private String imageBase64;

    public Base64CaptchaUpload(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    @Override
    public byte[] upload() {
        return Optional.ofNullable(imageBase64)
                .map(image -> Base64.getDecoder().decode(image.replaceAll("\\s", "")))
                .orElse(new byte[0]);
    }
}
