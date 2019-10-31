package io.riguron.captcha.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Data
@Setter(AccessLevel.NONE)
@Entity
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30, unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    public UserProfile(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
