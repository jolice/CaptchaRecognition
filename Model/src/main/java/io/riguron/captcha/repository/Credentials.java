package io.riguron.captcha.repository;

import lombok.Value;

@Value
public class Credentials {

    private final int id;
    private final String login;
    private final String password;
}
