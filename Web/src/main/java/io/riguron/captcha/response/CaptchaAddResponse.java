package io.riguron.captcha.response;

import lombok.Value;

@Value
public class CaptchaAddResponse {

    private boolean success;
    private int id;

}
