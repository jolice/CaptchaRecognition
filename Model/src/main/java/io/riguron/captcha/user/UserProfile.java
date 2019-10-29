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

    @Column(unique = true)
    private String login;

    @Column(unique = true, nullable = false)
    private String apiKey;

    @Column
    private UserBalance userBalance;

    @Column
    @Setter
    private CompleteRecognition completeRecognition;

    public UserProfile(String login, String apiKey) {
        this.login = login;
        this.apiKey = apiKey;
    }
}
