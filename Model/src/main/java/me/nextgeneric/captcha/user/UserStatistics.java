package me.nextgeneric.captcha.user;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
@AllArgsConstructor
public class UserStatistics {

    private int recognitions;

    private int solved;


}
