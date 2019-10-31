package io.riguron.captcha.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBalance {

    @Id
    private Integer id;

    private BigDecimal recognitionBalance;

    private BigDecimal earnedBalance;

    private int version;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    private UserProfile userProfile;

    public UserBalance(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
