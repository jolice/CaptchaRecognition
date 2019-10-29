package io.riguron.captcha.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.riguron.captcha.type.CaptchaType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaptchaGetResponse {

    private boolean success;
    private CaptchaType captchaType;
    private String contents;

    public CaptchaGetResponse(CaptchaType captchaType, String contents) {
        this.success = true;
        this.captchaType = captchaType;
        this.contents = contents;
    }

    public CaptchaGetResponse() {
        this.success = false;
    }


}
