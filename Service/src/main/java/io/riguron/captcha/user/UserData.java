package io.riguron.captcha.user;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Value
@EqualsAndHashCode(callSuper = true)
public class UserData extends User {

    private int id;

    public UserData(String username, String password, int id) {
        super(username, password, Collections.emptyList());
        this.id = id;
    }
}
