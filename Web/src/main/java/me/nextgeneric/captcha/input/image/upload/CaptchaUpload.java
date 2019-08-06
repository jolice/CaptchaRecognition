package me.nextgeneric.captcha.input.image.upload;

import java.io.IOException;

public interface CaptchaUpload {

    byte[] upload() throws IOException;
}
