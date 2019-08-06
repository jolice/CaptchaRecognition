package me.nextgeneric.captcha.user;

import lombok.*;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBalance {

    private BigDecimal recognitionBalance;

    private BigDecimal earnedBalance;

}
