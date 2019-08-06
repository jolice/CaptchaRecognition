package me.nextgeneric.captcha.solve;

import lombok.Value;

@Value
public class CaptchaSolution {

    private int captchaId;
    private int solverId;
    private String solution;

}
