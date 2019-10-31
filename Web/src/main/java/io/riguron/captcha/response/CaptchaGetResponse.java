package io.riguron.captcha.response;

import io.riguron.captcha.type.CaptchaType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Data
public class CaptchaGetResponse extends Response {

    private CaptchaType captchaType;
    private String contents;

    public CaptchaGetResponse(CaptchaType captchaType, String contents) {
        super(true);
        this.captchaType = captchaType;
        this.contents = contents;
    }


}
