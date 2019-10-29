package io.riguron.captcha.callback;

import io.riguron.captcha.type.Captcha;

public interface SolvingCallback {

    void captchaSolved(Captcha captcha);
}
