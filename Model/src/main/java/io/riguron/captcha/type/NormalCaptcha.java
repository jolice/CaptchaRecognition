package io.riguron.captcha.type;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class NormalCaptcha extends Captcha {

    @Lob
    @Column(length = 20971520)
    @ToString.Exclude
    private byte[] image;

    public NormalCaptcha(int originatorId, String callbackUrl, byte[] image) {
        super(originatorId, callbackUrl);
        this.image = image;
    }

    @Override
    public String getContents() {
        return new String(image);
    }

    @Override
    public CaptchaType getType() {
        return CaptchaType.IMAGE;
    }
}
