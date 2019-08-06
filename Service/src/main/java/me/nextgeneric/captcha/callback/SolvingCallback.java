package me.nextgeneric.captcha.callback;

import me.nextgeneric.captcha.type.Captcha;

public interface SolvingCallback {

    void captchaSolved(Captcha captcha);
}
