package io.riguron.captcha.callback;

import io.riguron.captcha.type.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("main")
public class RemoteSolvingCallback implements SolvingCallback {

    private RestTemplate restTemplate;

    @Autowired
    public RemoteSolvingCallback(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void captchaSolved(Captcha captcha) {
        if (captcha.getCallbackUrl() != null) {
            restTemplate.postForLocation(captcha.getCallbackUrl(), new CallbackCaptchaResponse(captcha.getId(), captcha.getFinalSolution()));
        }
    }
}
