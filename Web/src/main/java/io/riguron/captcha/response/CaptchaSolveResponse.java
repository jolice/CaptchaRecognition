package io.riguron.captcha.response;

import lombok.Value;

@Value
public class CaptchaSolveResponse {

    private boolean success;
    private String result;

}
