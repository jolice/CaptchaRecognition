package io.riguron.captcha.callback;

import lombok.Value;

@Value
public class CallbackCaptchaResponse {

    private int id;
    private String solution;

}
