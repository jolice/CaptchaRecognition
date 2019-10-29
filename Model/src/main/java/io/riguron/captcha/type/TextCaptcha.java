package io.riguron.captcha.type;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Setter(AccessLevel.NONE)
public class TextCaptcha extends Captcha {

    private String text;

    public TextCaptcha(int originatorId, String callbackUrl, String text) {
        super(originatorId, callbackUrl);
        this.text = text;
    }

    @Override
    public String getContents() {
        return text;
    }

    @Override
    public CaptchaType getType() {
        return CaptchaType.TEXT;
    }
}
