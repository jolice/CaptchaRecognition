package io.riguron.captcha;

import io.riguron.captcha.repository.CaptchaRepository;
import io.riguron.captcha.type.Captcha;

import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCaptchaConfigurer {

    public static final int CAPTCHA_ID = 10;

    public Captcha prepareCaptcha(Consumer<Captcha> configurer, CaptchaRepository captchaRepository) {
        Captcha captcha = mock(Captcha.class);
        when(captchaRepository.findById(CAPTCHA_ID)).thenAnswer(invocationOnMock -> {
            configurer.accept(captcha);
            return Optional.of(captcha);
        });
        return captcha;
    }
}
