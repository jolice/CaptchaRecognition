package me.nextgeneric.captcha;

import lombok.Data;
import me.nextgeneric.captcha.user.UserProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class UserRequestHolder {

    private UserProfile userProfile;

}
