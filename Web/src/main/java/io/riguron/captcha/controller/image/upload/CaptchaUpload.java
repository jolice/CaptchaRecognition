package io.riguron.captcha.controller.image.upload;

import java.io.IOException;

public interface CaptchaUpload {

    byte[] upload() throws IOException;
}
