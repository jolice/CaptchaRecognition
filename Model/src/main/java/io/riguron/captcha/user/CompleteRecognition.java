package io.riguron.captcha.user;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Embeddable
@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
public class CompleteRecognition {

    @Setter
    private boolean enabled;

    @Column
    @Min(2)
    @Max(7)
    private int minimumAttempts;

    @Column
    @Min(2)
    @Max(10)
    private int maximumAttempts;

    @Column
    @Min(2)
    @Max(5)
    private int minimumMatches;

    @AssertTrue
    public boolean checkBounds() {
        return maximumAttempts > minimumAttempts && minimumMatches <= minimumAttempts;
    }

}


