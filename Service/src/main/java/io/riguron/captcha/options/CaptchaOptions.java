package io.riguron.captcha.options;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CaptchaOptions {

    @Value("${captcha.retries_after_timeout}")
    private int retriesAfterSolveTimeout;

    @Value("${captcha.time_for_solving}")
    private int timeForSolving;

}
